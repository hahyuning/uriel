package com.uriel.travel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_detail_id")
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

    int flagCarrier;
    int tourConductor;
    int shopping;
    int optionalTour;

    int adultPrice;
    int adultSurcharge;
    int childPrice;
    int childSurcharge;
    int infantPrice;
    int infantSurcharge;

    @Lob
    String keyPoint;

    @Lob
    String includedProduct;

    @Lob
    String excludedProduct;

    @Lob
    String reservationNotice;

    @Lob
    String travelRegion;
}
