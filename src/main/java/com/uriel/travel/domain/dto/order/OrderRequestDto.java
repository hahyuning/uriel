package com.uriel.travel.domain.dto.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

public class OrderRequestDto {

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {

        String paymentKey;
        String amount;
        String orderId;

        Long productId;

        int adultCount;
        int childCount;
        int infantCount;
        int totalCount;
        Long totalPrice;

        List<TravelerInfo> travelerInfoList;
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UpdateTraveler {

        Long orderId;

        int adultCount;
        int childCount;
        int infantCount;
        int totalCount;

        List<TravelerInfo> travelerInfoList;
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AdditionalPayment {

        String paymentKey;
        String orderId;
        String amount;

        String imomOrderId;
    }
}
