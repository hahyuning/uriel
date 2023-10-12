package com.uriel.travel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import com.uriel.travel.domain.ProductState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponseDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime endDate;

    int remainingQuantity; // 잔여수량
    String airline;
    int price;
    ProductState productState;

    @QueryProjection
    public ProductResponseDto(LocalDateTime startDate, LocalDateTime endDate, String airline, int price, int maxCount, int nowCount, ProductState productState) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingQuantity = maxCount - nowCount;
        this.airline = airline;
        this.price = price;
        this.productState = productState;
    }

}
