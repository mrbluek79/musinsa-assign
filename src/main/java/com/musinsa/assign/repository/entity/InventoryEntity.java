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
@Table(name = "inventory")
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "count")
    private Long count;

    @CreationTimestamp
    @Column(name = "created_dt")
    private LocalDateTime createdDt;

    @UpdateTimestamp
    @Column(name = "updated_dt")
    private LocalDateTime updatedDt;
}
