package com.musinsa.assign.repository;

import com.musinsa.assign.repository.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    List<InventoryEntity> findAllByProductName(@Param("productName") String productName);

    InventoryEntity findByProductNameAndOptionName(
            @Param("productName") String productName,
            @Param("optionName") String optionName
    );
}
