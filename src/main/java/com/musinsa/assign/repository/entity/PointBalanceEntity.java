package com.musinsa.assign.repository.entity;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "point_balance")
public class PointBalanceEntity {
    @Id
    private Long userId;

    private Long point;

    @UpdateTimestamp
    @Column(name = "updated_dt")
    private LocalDateTime updatedDt;
}
