package com.musinsa.assign.repository;

import com.musinsa.assign.repository.entity.PointEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, Long> {
    Page<PointEntity> findByUserIdOrderByCreatedDtDesc(Long userId, Pageable pageable);

    @Query("select p from PointEntity p where p.userId = :userId and p.useYn = 1 and p.type in (1, 4) and p.expiredDt < :now")
    List<PointEntity> expiredList(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("select p from PointEntity p where p.userId = :userId and p.useYn = 1 and p.type in (1, 4) and p.expiredDt >= :now order by p.createdDt asc")
    List<PointEntity> usablePointList(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    List<PointEntity> findByUserIdAndTransactionNo(Long userId, Long transactionNo);

    @Modifying
    @Query("update PointEntity p set p.useYn = 0, p.updatedDt = :now where p.id = :id")
    void expire(@Param("id") Long id, @Param("now") LocalDateTime now);

    @Modifying
    @Query("update PointEntity p set p.remainPoint = :remainPoint, p.updatedDt = :now where p.id = :id")
    void usePartial(@Param("id") Long id, @Param("remainPoint") Long remainPoint, @Param("now") LocalDateTime now);

    @Modifying
    @Query("update PointEntity p set p.useYn = 0, p.remainPoint = 0L, p.updatedDt = :now where p.id = :id")
    void useAll(@Param("id") Long id, @Param("now") LocalDateTime now);
}
