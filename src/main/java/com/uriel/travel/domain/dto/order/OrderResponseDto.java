package com.uriel.travel.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uriel.travel.domain.entity.Order;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class OrderResponseDto {

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderInfo {
        String imomOrderId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime orderDate;

        String reserveUser;
        LocalDate birth;
        String gender;
        String email;
        String phoneNumber;

        String packageName;
        String productCode;

        String orderState;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int adultCount;
        int childCount;
        int infantCount;
        int totalCount;

        Long totalPrice;
        Long payedPrice;
        Long balance;

        List<TravelerInfo> travelerInfos;

        public static OrderInfo of(Order order) {
            return OrderInfo.builder()
                    .imomOrderId(order.getImomOrderId())
                    .orderDate(order.getOrderDate())
                    .reserveUser(order.getReserveUser().getKrName())
                    .birth(order.getReserveUser().getBirth())
                    .gender(order.getReserveUser().getGender().getViewName())
                    .email(order.getReserveUser().getEmail())
                    .phoneNumber(order.getReserveUser().getPhoneNumber())
                    .packageName(order.getProduct().getAPackage().getPackageName())
                    .productCode(order.getProduct().getProductCode())
                    .orderState(order.getOrderState().getViewName())
                    .startDate(order.getProduct().getStartDate())
                    .endDate(order.getProduct().getEndDate())
                    .adultCount(order.getAdultCount())
                    .childCount(order.getChildCount())
                    .infantCount(order.getInfantCount())
                    .totalCount(order.getTotalCount())
                    .totalPrice(order.getTotalPrice())
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
        String imomOrderId;

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
                    .imomOrderId(order.getImomOrderId())
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
