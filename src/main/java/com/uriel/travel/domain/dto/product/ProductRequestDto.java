package com.uriel.travel.domain.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uriel.travel.domain.Airline;
import com.uriel.travel.domain.entity.Product;
import com.uriel.travel.domain.ProductState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

public class ProductRequestDto {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {

        Long packageId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int minCount;
        int maxCount;
        String productState;
        String airline;
        String privacy;
        int price;

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

        public Product toEntity() {
            return Product.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .minCount(minCount)
                    .maxCount(maxCount)
                    .productState(ProductState.from(productState))
                    .airline(Airline.from(airline))
                    .price(price)
                    .build();
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {

        String privacy;
        Long packageId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int minCount;
        int maxCount;
        String productState;
        String airline;
        int price;

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
    }
}
