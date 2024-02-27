package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderState implements CodedEnum<String> {

    COMPLETE("예약 완료"),
    FULL_PAYMENT("완납"),
    CANCELED("취소");

    private final String viewName;

    @JsonCreator
    public static OrderState from(String sub) {
        for (OrderState orderState : OrderState.values()) {
            if (orderState.getViewName().equals(sub)) {
                return orderState;
            }
        }
        return null;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<OrderState, String> {
        public Converter() {
            super(OrderState.class);
        }
    }
}
