package com.uriel.travel.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uriel.travel.domain.entity.Order;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;


public class OrderResponseDto {

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderInfo {
        String orderNumber;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime orderDate;

        String reserveUser;

        String packageName;

        String productCode;

        String orderState;

        String method;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int adultCount;
        int childCount;
        int infantCount;
        int totalCount;

        int payedPrice;
        int balance;

        List<TravelerInfo> travelerInfos;

        public static OrderInfo of(Order order) {
            return OrderInfo.builder()
                    .orderNumber(order.getOrderNumber())
                    .orderDate(order.getOrderDate())
                    .reserveUser(order.getReserveUser().getKrName())
                    .packageName(order.getProduct().getAPackage().getPackageName())
                    .productCode(order.getProduct().getProductCode())
                    .orderState(order.getOrderState().getViewName())
                    .method(order.getMethod())
                    .startDate(order.getProduct().getStartDate())
                    .endDate(order.getProduct().getEndDate())
                    .adultCount(order.getAdultCount())
                    .childCount(order.getChildCount())
                    .infantCount(order.getInfantCount())
                    .totalCount(order.getTotalCount())
                    .payedPrice(order.getPayedPrice())
                    .balance(order.getTotalPrice() - order.getPayedPrice())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MyOrder {
        String orderNumber;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime orderDate;

        String packageName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int totalCount;

        String productState;
        String orderState;

        public static MyOrder of(Order order) {
            return MyOrder.builder()
                    .orderNumber(order.getOrderNumber())
                    .orderDate(order.getOrderDate())
                    .packageName(order.getProduct().getAPackage().getPackageName())
                    .startDate(order.getProduct().getStartDate())
                    .endDate(order.getProduct().getEndDate())
                    .totalCount(order.getTotalCount())
                    .productState(order.getProduct().getProductState().getViewName())
                    .orderState(order.getOrderState().getViewName())
                    .build();
        }
    }
}
