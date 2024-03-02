package com.uriel.travel.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.uriel.travel.domain.OrderState;
import com.uriel.travel.domain.ProductState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

public class OrderFilter {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderFilterCond {

        LocalDateTime orderDateMin;
        LocalDateTime orderDateMax;

        Long packageId;
        String country;
        String orderState;

        Long userNameOrder;
        Long order;
        Long start;

        int offset;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderSearchCond {

        String type;
        String target;
        int offset;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderFilterResponseDtoForAdmin {

        String orderNumber;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
        LocalDateTime orderDate;
        String orderState;

        String packageName;
        String productCode;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;

        String reserveUserName;
        String phoneNumber;
        String email;
        int totalCount;

        int payedPrice;
        int balance;

        @QueryProjection
        public OrderFilterResponseDtoForAdmin(String orderNumber, LocalDateTime orderDate, OrderState orderState, String packageName, String productCode, LocalDateTime startDate,
                                              String reserveUserName, String phoneNumber, String email, int totalCount, int totalPrice, int payedPrice) {
            this.orderNumber = orderNumber;
            this.orderDate = orderDate;
            this.orderState = orderState.getViewName();
            this.packageName = packageName;
            this.productCode = productCode;
            this.startDate = startDate;
            this.reserveUserName = reserveUserName;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.totalCount = totalCount;
            this.payedPrice = payedPrice;
            this.balance = totalPrice - payedPrice;
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderFilterResponseDto {

        String orderNumber;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
        LocalDateTime orderDate;
        String orderState;

        String packageName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;

        int totalCount;
        String productState;

        @QueryProjection
        public OrderFilterResponseDto(String orderNumber, LocalDateTime orderDate, OrderState orderState,
                                      String packageName, LocalDateTime startDate, int totalCount, ProductState productState) {
            this.orderNumber = orderNumber;
            this.orderDate = orderDate;
            this.orderState = orderState.getViewName();
            this.packageName = packageName;
            this.startDate = startDate;
            this.totalCount = totalCount;
            this.productState = productState.getViewName();
        }
    }
}
