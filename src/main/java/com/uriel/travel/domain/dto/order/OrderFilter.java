package com.uriel.travel.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.uriel.travel.domain.Country;
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

        String type;
        String target;

        int offset;
    }

//    @Getter
//    @Setter
//    @FieldDefaults(level = AccessLevel.PRIVATE)
//    public static class OrderSearchCond {
//
//        String type;
//        String target;
//        int offset;
//    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderFilterResponseDtoForAdmin {

        String imomOrderId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime orderDate;
        String orderState;

        String packageName;
        String country;
        String productCode;
        String productState;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;

        String reserveUser;
        String phoneNumber;
        String email;
        int totalCount;

        @QueryProjection
        public OrderFilterResponseDtoForAdmin(String imomOrderId, LocalDateTime orderDate, OrderState orderState, String packageName, Country country, String productCode, ProductState productState, LocalDateTime startDate,
                                              String reserveUser, String phoneNumber, String email, int totalCount) {
            this.imomOrderId = imomOrderId;
            this.orderDate = orderDate;
            this.orderState = orderState.getViewName();
            this.packageName = packageName;
            this.country = country.getViewName();
            this.productCode = productCode;
            this.productState = productState.getViewName();
            this.startDate = startDate;
            this.reserveUser = reserveUser;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.totalCount = totalCount;
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderFilterResponseDto {

        String imomOrderId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime orderDate;
        String orderState;

        String packageName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;

        int totalCount;
        String productState;

        @QueryProjection
        public OrderFilterResponseDto(String imomOrderId, LocalDateTime orderDate, OrderState orderState,
                                      String packageName, LocalDateTime startDate, int totalCount, ProductState productState) {
            this.imomOrderId = imomOrderId;
            this.orderDate = orderDate;
            this.orderState = orderState.getViewName();
            this.packageName = packageName;
            this.startDate = startDate;
            this.totalCount = totalCount;
            this.productState = productState.getViewName();
        }
    }
}
