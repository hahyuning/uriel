package com.uriel.travel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.uriel.travel.domain.Product;
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
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Create {

        Long packageId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int minCount;
        int maxCount;
        ProductState productState;
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

        public Product toEntity() {
            return Product.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .minCount(minCount)
                    .maxCount(maxCount)
                    .productState(productState)
                    .airline(airline)
                    .price(price)
                    .build();
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Update {

        int privacy;
        Long packageId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int minCount;
        int maxCount;
        ProductState productState;
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

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FilterCond {

        Long packageId;
        ProductState productState;
        LocalDateTime startDateMin;
        LocalDateTime startDateMax;
        int offset;
        int limit;
    }
}
