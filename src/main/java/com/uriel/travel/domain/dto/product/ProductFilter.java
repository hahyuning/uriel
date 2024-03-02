package com.uriel.travel.domain.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.uriel.travel.domain.Airline;
import com.uriel.travel.domain.ProductState;
import com.uriel.travel.domain.Release;
import com.uriel.travel.domain.SaveState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

public class ProductFilter {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProductFilterCond {

        Long packageId;
        String productState;
        String privacy;
        String saveState;

        LocalDateTime startDateMin;
        LocalDateTime startDateMax;

        Integer startDateOrder;
        Integer endDateOrder;

        int offset;
        int limit;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProductFilterResponseDto {

        Long productId;
        String productCode;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int maxCount;
        int nowCount;
        String airline;
        int price;
        String productState;

        @QueryProjection
        public ProductFilterResponseDto(Long id, String productCode, LocalDateTime startDate, LocalDateTime endDate, Airline airline, int price, int maxCount, int nowCount, ProductState productState) {
            this.productId = id;
            this.productCode = productCode;
            this.startDate = startDate;
            this.endDate = endDate;
            this.maxCount = maxCount;
            this.nowCount = nowCount;
            this.airline = airline.getViewName();
            this.price = price;
            this.productState = productState.getViewName();
        }

    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProductFilterForAdminResponseDto {

        Long productId;

        String packageName;
        String productCode;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int minCount;
        int maxCount;
        int nowCount;

        String productState;
        String saveState;
        String privacy;

        @QueryProjection
        public ProductFilterForAdminResponseDto(Long id, String packageName, String productCode, LocalDateTime startDate, LocalDateTime endDate, int minCount, int maxCount, int nowCount, ProductState productState, SaveState saveState, Release privacy) {
            this.productId = id;
            this.packageName = packageName;
            this.productCode = productCode;
            this.startDate = startDate;
            this.endDate = endDate;
            this.minCount = minCount;
            this.maxCount = maxCount;
            this.nowCount = nowCount;
            this.productState = productState.getViewName();
            this.saveState = saveState.getViewName();
            this.privacy = privacy.getViewName();
        }

    }
}
