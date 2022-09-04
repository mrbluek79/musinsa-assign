package com.musinsa.assign;

import com.musinsa.assign.repository.InventoryRepository;
import com.musinsa.assign.repository.entity.InventoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class InitData {
    private final InventoryRepository repository;

    @PostConstruct
    public void creation() {
        repository.save(InventoryEntity.builder().productName("prd-a").optionName("opt-aa").count(0L).createdDt(LocalDateTime.now()).updatedDt(LocalDateTime.now()).build());
        repository.save(InventoryEntity.builder().productName("prd-a").optionName("opt-ab").count(0L).createdDt(LocalDateTime.now()).updatedDt(LocalDateTime.now()).build());
        repository.save(InventoryEntity.builder().productName("prd-b").optionName("opt-ba").count(0L).createdDt(LocalDateTime.now()).updatedDt(LocalDateTime.now()).build());
        repository.save(InventoryEntity.builder().productName("prd-b").optionName("opt-bb").count(0L).createdDt(LocalDateTime.now()).updatedDt(LocalDateTime.now()).build());
        repository.save(InventoryEntity.builder().productName("prd-b").optionName("opt-bc").count(0L).createdDt(LocalDateTime.now()).updatedDt(LocalDateTime.now()).build());
        repository.save(InventoryEntity.builder().productName("prd-c").optionName("opt-ca").count(0L).createdDt(LocalDateTime.now()).updatedDt(LocalDateTime.now()).build());
    }
}
