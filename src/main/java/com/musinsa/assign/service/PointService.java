package com.musinsa.assign.service;

import com.musinsa.assign.api.controller.dto.PointDto;
import com.musinsa.assign.enums.PointTypeEnum;
import com.musinsa.assign.repository.PointBalanceRepository;
import com.musinsa.assign.repository.PointRepository;
import com.musinsa.assign.repository.entity.PointBalanceEntity;
import com.musinsa.assign.repository.entity.PointEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {
    private final PointRepository pointRepository;
    private final PointBalanceRepository pointBalanceRepository;

    public PointBalanceEntity getPointBalance(Long userId) {
        try {
            pointExpire(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return pointBalanceRepository.findById(userId).orElse(null);
    }

    public Page<PointEntity> getUserPointHistory(Long userId, int pageNo) {
        try {
            pointExpire(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        int PAGE_SIZE = 10;
        return pointRepository.findByUserIdOrderByCreatedDtDesc(userId, PageRequest.of(pageNo - 1, PAGE_SIZE));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public PointDto.SuccessResponse savePoint(Long userId, Long point, Long transactionNo) throws Exception {
        PointBalanceEntity pointBalanceEntity = pointBalanceRepository.findById(userId).orElse(null);

        // 1. Balance에 저장
        if (pointBalanceEntity == null) {
            // 최초일 경우
            pointBalanceRepository.save(PointBalanceEntity.builder()
                    .userId(userId)
                    .point(point)
                    .updatedDt(LocalDateTime.now())
                    .build());
        } else {
            pointExpire(userId);
            pointBalanceRepository.savePoint(userId, point + pointBalanceEntity.getPoint());
        }

        // 2. Point에 이력 저장
        pointRepository.save(PointEntity.builder()
                .userId(userId)
                .point(point)
                .remainPoint(point)
                .transactionNo(transactionNo)
                .type(PointTypeEnum.SAVE.getValue())
                .useYn(1)
                .expiredDt(LocalDateTime.now().plusYears(1L))
                .createdDt(LocalDateTime.now())
                .build());

        return PointDto.SuccessResponse.builder().success(true).build();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public PointDto.SuccessResponse usePoint(Long userId, Long point, Long transactionNo) throws Exception {
        pointExpire(userId);
        PointBalanceEntity pointBalanceEntity = pointBalanceRepository.findById(userId).orElse(null);

        if (pointBalanceEntity == null) {
            // Exception
            throw new Exception("Point Balance 데이터가 없습니다.");
        } else {
            long balancePoint = pointBalanceEntity.getPoint() - point;

            if (balancePoint < 0) {
                // Exception
                throw new Exception("사용할 포인트는 보유 보인트를 초과할 수 없습니다.");
            }

            pointBalanceRepository.savePoint(userId, balancePoint);

            // 포인트 사용처리
            LocalDateTime now = LocalDateTime.now();
            List<PointEntity> usablePointList = pointRepository.usablePointList(userId, now);
            for(PointEntity usablePoint : usablePointList) {
                long usedPoint;
                if (point < usablePoint.getRemainPoint()) {
                    pointRepository.usePartial(usablePoint.getId(), usablePoint.getRemainPoint() - point, now);

                    usedPoint = point;
                    point = 0L;
                } else {
                    pointRepository.useAll(usablePoint.getId(), now);

                    usedPoint = usablePoint.getRemainPoint();
                    point -= usablePoint.getRemainPoint();
                }

                // 사용이력 저장
                pointRepository.save(PointEntity.builder()
                        .userId(userId)
                        .pid(usablePoint.getId())
                        .transactionNo(transactionNo)
                        .type(PointTypeEnum.USE.getValue())
                        .point(usedPoint)
                        .expiredDt(usablePoint.getExpiredDt())
                        .createdDt(now)
                        .build());

                if (point == 0L) break;
            }
        }

        return PointDto.SuccessResponse.builder().success(true).build();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public PointDto.SuccessResponse cancelPoint(Long transactionNo, Long userId) throws Exception {
        pointExpire(userId);
        PointBalanceEntity pointBalanceEntity = pointBalanceRepository.findById(userId).orElse(null);

        if (pointBalanceEntity == null) {
            // Exception
            throw new Exception("Point Balance 데이터가 없습니다.");
        } else {
            List<PointEntity> cancelEntities = pointRepository.findByUserIdAndTransactionNo(userId, transactionNo);

            if (cancelEntities == null) {
                // Exception
                throw new Exception("취소할 데이터가 없습니다.");
            } else {
                LocalDateTime now = LocalDateTime.now();
                Long rollbackPoint = pointBalanceEntity.getPoint() + cancelEntities.stream().mapToLong(PointEntity::getPoint).sum();
                pointBalanceRepository.savePoint(userId, rollbackPoint);

                for (PointEntity cancelEntity : cancelEntities) {
                    pointRepository.save(PointEntity.builder()
                            .userId(userId)
                            .point(cancelEntity.getPoint())
                            .remainPoint(cancelEntity.getPoint())
                            .transactionNo(transactionNo)
                            .type(PointTypeEnum.CANCEL_USE.getValue())
                            .useYn(1)
                            .expiredDt(cancelEntity.getExpiredDt())
                            .createdDt(now)
                            .build());
                }
            }
        }

        return PointDto.SuccessResponse.builder().success(true).build();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    void pointExpire(Long userId) throws Exception {
        PointBalanceEntity pointBalanceEntity = pointBalanceRepository.findById(userId).orElse(null);
        if (pointBalanceEntity != null) {
            List<PointEntity> expiredList = pointRepository.expiredList(userId, LocalDateTime.now());
            long expiredPoint = expiredList.stream().mapToLong(PointEntity::getRemainPoint).sum();

            if (expiredPoint > pointBalanceEntity.getPoint()) {
                throw new Exception("system error occurred");
            }

            pointBalanceEntity.setPoint(pointBalanceEntity.getPoint() - expiredPoint);
            for (PointEntity expiredPointEntity : expiredList) {
                pointRepository.expire(expiredPointEntity.getId(), LocalDateTime.now());
            }
        }
    }
}
