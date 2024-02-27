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
        String orderId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime orderedDate;

        String reserveUser;

        String packageName;

        String productCode;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int adultCount;
        int childCount;
        int infantCount;
        int totalCount;

        int deposit;
        int balance;

        List<TravelerInfo> travelerInfos;

        public static OrderInfo of(Order order) {
            return OrderInfo.builder()
                    .orderId(order.getOrderId())
                    .orderedDate(order.getOrderedDate())
                    .reserveUser(order.getReserveUser().getKrName())
                    .packageName(order.getAPackage().getPackageName())
                    .productCode(order.getProduct().getProductCode())
                    .startDate(order.getProduct().getStartDate())
                    .endDate(order.getProduct().getEndDate())
                    .adultCount(order.getAdultCount())
                    .childCount(order.getChildCount())
                    .infantCount(order.getInfantCount())
                    .totalCount(order.getAdultCount() + order.getChildCount() + order.getInfantCount())
                    .deposit(order.getDeposit())
                    .balance(order.getBalance())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MyOrder {
        String orderId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime orderedDate;

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
                    .orderId(order.getOrderId())
                    .orderedDate(order.getOrderedDate())
                    .packageName(order.getAPackage().getPackageName())
                    .startDate(order.getProduct().getStartDate())
                    .endDate(order.getProduct().getEndDate())
                    .totalCount(order.getAdultCount() + order.getChildCount() + order.getInfantCount())
                    .productState(order.getProduct().getProductState().getViewName())
                    .orderState(order.getOrderState().getViewName())
                    .build();
        }
    }
}
