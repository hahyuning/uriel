package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderSearchType {

    USERNAME("예약자명"),
    PHONE_NUMBER("이메일"),
    ORDER_NUMBER("주문번호");

    private final String viewName;

    @JsonCreator
    public static OrderSearchType from(String sub) {
        for (OrderSearchType type : OrderSearchType.values()) {
            if (type.getViewName().equals(sub)) {
                return type;
            }
        }
        return null;
    }
}
