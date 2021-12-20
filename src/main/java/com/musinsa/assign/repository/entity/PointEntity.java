package com.musinsa.assign.repository.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
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
@Table(name = "point")
public class PointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "pid")
    private Long pid;

    @Column(name = "transaction_no")
    private Long transactionNo;

    @Column(name = "user_id")
    private Long userId;

    private Long point;

    @Column(name = "remain_point")
    private Long remainPoint;

    private Integer type;

    @Column(name = "use_yn")
    private Integer useYn;

    @Column(name = "expired_dt")
    private LocalDateTime expiredDt;

    @CreationTimestamp
    @Column(name = "created_dt")
    private LocalDateTime createdDt;

    @UpdateTimestamp
    @Column(name = "updated_dt")
    private LocalDateTime updatedDt;
}
