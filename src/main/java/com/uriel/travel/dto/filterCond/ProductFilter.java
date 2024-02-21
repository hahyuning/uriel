package com.uriel.travel.dto.filterCond;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.uriel.travel.domain.ProductState;
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
        ProductState productState;
        LocalDateTime startDateMin;
        LocalDateTime startDateMax;
        int offset;
        int limit;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProductFilterResponseDto {

        Long productId;
        String productCode;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int maxCount;
        int nowCount;
        String airline;
        int price;
        String productState;

        @QueryProjection
        public ProductFilterResponseDto(Long id, String productCode, LocalDateTime startDate, LocalDateTime endDate, String airline, int price, int maxCount, int nowCount, ProductState productState) {
            this.productId = id;
            this.productCode = productCode;
            this.startDate = startDate;
            this.endDate = endDate;
            this.maxCount = maxCount;
            this.nowCount = nowCount;
            this.airline = airline;
            this.price = price;
            this.productState = productState.getViewName();
        }

    }
}
