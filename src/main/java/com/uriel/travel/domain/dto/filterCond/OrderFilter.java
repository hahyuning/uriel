package com.uriel.travel.domain.dto.filterCond;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

public class OrderFilter {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderFilterCond {

        LocalDate orderedDateMin;
        LocalDate orderedDateMax;

        Long packageId;
        String country;
        String orderState;

        int userNameOrder;
        int orderedDateOrder;
        int startDateOrder;

        int offset;
    }


    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderSearchCond {

        String searchType;

        String phoneNumber;
        String email;
        String orderNumber;
        String reserveUserName;
    }


}
