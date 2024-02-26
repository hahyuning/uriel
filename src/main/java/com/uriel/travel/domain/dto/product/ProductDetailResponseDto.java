package com.uriel.travel.domain.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uriel.travel.domain.dto.travelPackage.PackageResponseDto;
import com.uriel.travel.domain.entity.Product;
import com.uriel.travel.domain.entity.ProductDetail;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponseDto {

    PackageResponseDto.PackageInfo packageInfo;
    GetProduct productInfo;

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class GetProduct {

        Long productId;
        String productCode;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endDate; // 도착일시

        int minCount; // 최소 출발인원
        int maxCount; // 최대 예약인원
        int nowCount; // 현재 예약인원
        String productState; // 상품 상태
        String airline; // 항공사
        int price; // 가격

        int flagCarrier; // 비행기 국적
        int tourConductor; // 인솔자 동행
        int shopping; // 쇼핑 유무
        int optionalTour; // 선택 관광 유무

        int adultPrice; // 성인 요금
        int adultSurcharge; // 성인 유류 할증
        int childPrice; // 아동 요금
        int childSurcharge; // 아동 유류 할증
        int infantPrice; // 유아 요금
        int infantSurcharge; // 유아 유류 할증

        String keyPoint; // 핵심 키포인트
        String includedProduct; // 포함 내역
        String excludedProduct; // 불포함 내역
        String reservationNotice; // 예약 유의사항
        String travelRegion; // 여행지역

        public static GetProduct of(Product product, ProductDetail productDetail) {
            return GetProduct.builder()
                    .productCode(product.getProductCode())
                    .productId(product.getId())
                    .startDate(product.getStartDate())
                    .endDate(product.getEndDate())
                    .minCount(product.getMinCount())
                    .maxCount(product.getMaxCount())
                    .nowCount(product.getNowCount())
                    .productState(product.getProductState().getViewName())
                    .airline(product.getAirline().getViewName())
                    .price(product.getPrice())
                    .flagCarrier(productDetail.getFlagCarrier())
                    .tourConductor(productDetail.getTourConductor())
                    .shopping(productDetail.getShopping())
                    .optionalTour(productDetail.getOptionalTour())
                    .adultPrice(productDetail.getAdultPrice())
                    .adultSurcharge(productDetail.getAdultSurcharge())
                    .childPrice(productDetail.getChildPrice())
                    .childSurcharge(productDetail.getChildSurcharge())
                    .infantPrice(productDetail.getInfantPrice())
                    .infantSurcharge(productDetail.getInfantSurcharge())
                    .keyPoint(productDetail.getKeyPoint())
                    .includedProduct(productDetail.getIncludedProduct())
                    .excludedProduct(productDetail.getExcludedProduct())
                    .reservationNotice(productDetail.getReservationNotice())
                    .travelRegion(productDetail.getTravelRegion())
                    .build();
        }
    }
}

