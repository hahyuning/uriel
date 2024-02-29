package com.uriel.travel.domain.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRequestDto {

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {

        String paymentKey;

        String orderNumber;

        LocalDateTime orderDate;

        String method;

        Long productId;

        int adultCount;
        int childCount;
        int infantCount;

        int totalPrice;
        int payedPrice;

        String orderState;

        List<TravelerInfo> travelerInfoList;
    }
}
