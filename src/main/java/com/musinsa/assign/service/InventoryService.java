package com.musinsa.assign.service;

import com.musinsa.assign.api.controller.dto.InventoryDto;
import com.musinsa.assign.exception.DataNotFoundException;
import com.musinsa.assign.repository.InventoryRepository;
import com.musinsa.assign.repository.entity.InventoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class InventoryService {
    private final InventoryRepository repository;

    public List<InventoryEntity> getInventories(String productName) {
        return repository.findAllByProductName(productName);
    }

    public InventoryEntity getInventory(String productName, String optionName) {
        return repository.findByProductNameAndOptionName(productName, optionName);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean increase(InventoryDto.SaveRequest request) {
        InventoryEntity entity = repository.findByProductNameAndOptionName(request.getProductName(), request.getOptionName());

        if (entity == null) {
            entity = InventoryEntity.builder()
                    .productName(request.getProductName())
                    .optionName(request.getOptionName())
                    .count(request.getCount())
                    .createdDt(LocalDateTime.now())
                    .updatedDt(LocalDateTime.now())
                    .build();
        } else {
            entity.setCount(entity.getCount() + request.getCount());
        }

        repository.save(entity);

        return true;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean subtract(InventoryDto.SaveRequest request) throws Exception {
        InventoryEntity entity = repository.findByProductNameAndOptionName(request.getProductName(), request.getOptionName());

        if (entity == null) throw new DataNotFoundException("상품 재고 정보가 존재하지 않습니다.");

        if (entity.getCount() - request.getCount() < 0) throw new Exception("차감 수량이 현재 재고 수량보다 많습니다.");

        entity.setCount(entity.getCount() - request.getCount());
        repository.save(entity);

        return true;
    }
}
