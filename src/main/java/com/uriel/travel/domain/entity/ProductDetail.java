package com.uriel.travel.domain.entity;

import com.uriel.travel.domain.dto.product.ProductRequestDto;
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

    int flagCarrier; // 비행기 국적
    int tourConductor; // 인솔자 동행
    int shopping; // 쇼핑 유무
    int optionalTour; // 선택 관광 유무

    Long adultPrice; // 성인 요금
    Long adultSurcharge; // 성인 유류 할증
    Long childPrice; // 아동 요금
    Long childSurcharge; // 아동 유류 할증
    Long infantPrice; // 유아 요금
    Long infantSurcharge; // 유아 유류 할증

    @Lob
    String keyPoint; // 핵심 키포인트

    @Lob
    String includedProduct; // 포함 내역

    @Lob
    String excludedProduct; // 불포함 내역

    @Lob
    String reservationNotice; // 예약 유의사항

    @Lob
    String travelRegion; // 여행지역

    public ProductDetail(ProductRequestDto.Create requestDto, Product product) {
        this.flagCarrier = requestDto.getFlagCarrier();
        this.tourConductor = requestDto.getTourConductor();
        this.shopping = requestDto.getShopping();
        this.optionalTour = requestDto.getOptionalTour();
        this.adultPrice = requestDto.getAdultPrice();
        this.adultSurcharge = requestDto.getAdultSurcharge();
        this.childPrice = requestDto.getChildPrice();
        this.childSurcharge = requestDto.getChildSurcharge();
        this.infantPrice = requestDto.getInfantPrice();
        this.infantSurcharge = requestDto.getInfantSurcharge();
        this.keyPoint = requestDto.getKeyPoint();
        this.includedProduct = requestDto.getIncludedProduct();
        this.excludedProduct = requestDto.getExcludedProduct();
        this.reservationNotice = requestDto.getReservationNotice();
        this.travelRegion = requestDto.getTravelRegion();

        this.product = product;
    }

    public void update(ProductRequestDto.Update requestDto) {
        this.flagCarrier = requestDto.getFlagCarrier();
        this.tourConductor = requestDto.getTourConductor();
        this.shopping = requestDto.getShopping();
        this.optionalTour = requestDto.getOptionalTour();
        this.adultPrice = requestDto.getAdultPrice();
        this.adultSurcharge = requestDto.getAdultSurcharge();
        this.childPrice = requestDto.getChildPrice();
        this.childSurcharge = requestDto.getChildSurcharge();
        this.infantPrice = requestDto.getInfantPrice();
        this.infantSurcharge = requestDto.getInfantSurcharge();
        this.keyPoint = requestDto.getKeyPoint();
        this.includedProduct = requestDto.getIncludedProduct();
        this.excludedProduct = requestDto.getExcludedProduct();
        this.reservationNotice = requestDto.getReservationNotice();
        this.travelRegion = requestDto.getTravelRegion();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void idInitialize() {
        this.id = null;
    }
}
