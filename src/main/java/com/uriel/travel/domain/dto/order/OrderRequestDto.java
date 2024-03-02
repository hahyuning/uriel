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

        Long productId;

        int adultCount;
        int childCount;
        int infantCount;
        int totalCount;
        int totalPrice;

        List<TravelerInfo> travelerInfoList;
    }
}
