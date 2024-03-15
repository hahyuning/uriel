package com.uriel.travel.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uriel.travel.domain.entity.Order;
import com.uriel.travel.domain.entity.Product;
import com.uriel.travel.domain.entity.User;
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
        Long additionalPrice;
        Long balance;

        List<String> orderNumberList;
        List<TravelerInfo> travelerInfos;

        String memo;

        public static OrderInfo of(Order order) {
            return OrderInfo.builder()
                    .imomOrderId(order.getImomOrderId())
                    .orderDate(order.getOrderDate())
                    .productCode(order.getProductCode())
                    .orderState(order.getOrderState().getViewName())
                    .adultCount(order.getAdultCount())
                    .childCount(order.getChildCount())
                    .infantCount(order.getInfantCount())
                    .totalCount(order.getTotalCount())
                    .totalPrice(order.getTotalPrice() + order.getAdditionalPrice())
                    .payedPrice(order.getPayedPrice())
                    .additionalPrice(order.getAdditionalPrice())
                    .balance(order.getTotalPrice() - order.getPayedPrice() + order.getAdditionalPrice())
                    .orderNumberList(order.getOrderNumberList())
                    .memo(order.getMemo())
                    .build();
        }

        public void setReserveUserInfo(User user) {
            this.reserveUser = user.getKrName();
            this.birth = user.getBirth();
            this.gender = user.getGender().getViewName();
            this.email = user.getEmail();
            this.phoneNumber = user.getPhoneNumber();
        }

        public void setProductInfo(Product product) {
            this.packageName = product.getAPackage().getPackageName();
            this.startDate = product.getStartDate();
            this.endDate = product.getEndDate();
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

        String reserveUser;

        String packageName;
        String productCode;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime endDate;

        int totalCount;
        int adultCount;
        int childCount;
        int infantCount;

        Long totalPrice;
        Long payedPrice;
        Long balance;

        List<TravelerInfo> travelerInfos;

        public static MyOrder of(Order order) {
            return MyOrder.builder()
                    .imomOrderId(order.getImomOrderId())
                    .orderDate(order.getOrderDate())
                    .productCode(order.getProductCode())
                    .totalCount(order.getTotalCount())
                    .adultCount(order.getAdultCount())
                    .childCount(order.getChildCount())
                    .infantCount(order.getInfantCount())
                    .totalPrice(order.getTotalPrice() + order.getAdditionalPrice())
                    .payedPrice(order.getPayedPrice())
                    .balance(order.getTotalPrice() - order.getPayedPrice() + order.getAdditionalPrice())
                    .build();
        }

        public void setReserveUserInfo(User user) {
            this.reserveUser = user.getKrName();
        }

        public void setProductInfo(Product product) {
            this.packageName = product.getAPackage().getPackageName();
            this.startDate = product.getStartDate();
            this.endDate = product.getEndDate();
        }
    }
}
