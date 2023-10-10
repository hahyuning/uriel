package com.uriel.travel.domain;

import com.uriel.travel.domain.enumeration.ProductState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    Long id;
    int privacy;
    @Column(unique = true)
    String productCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    Package aPackage;
    LocalDateTime startDate;
    LocalDateTime endDate;
    int minCount; // 최소 출발인원
    int maxCount; // 최대 예약인원
    int nowCount; // 현재 예약인원
    @Enumerated(EnumType.STRING)
    ProductState productState;
    String airline;
    int price;
}
