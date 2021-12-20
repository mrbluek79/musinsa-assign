package com.musinsa.assign.repository;

import com.musinsa.assign.repository.entity.PointBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointBalanceRepository extends JpaRepository<PointBalanceEntity, Long> {
    @Modifying
    @Query("update PointBalanceEntity pb set pb.point = :point where pb.userId = :userId")
    void savePoint(@Param("userId") Long userId, @Param("point") Long point);
}
