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

        String orderId;

        LocalDateTime orderedDate;

        Long productId;

        int adultCount;
        int childCount;
        int infantCount;

        String orderState;

        int totalPrice;

        List<TravelerInfo> travelerInfoList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {

        String orderId;
        int balance;
        String orderState;
    }
}
