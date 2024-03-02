package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderState implements CodedEnum<String> {

    NEEDED_PAYMENT("결제 대기"),
    PARTIAL_PAYMENT("예약금 납부 완료"),
    FULL_PAYMENT("전액 납부 완료"),
    NEEDED_ADDITIONAL_PAYMENT("추가 결제 필요"),
    REFUND_NEEDED("환불 필요"),
    REFUND_COMPLETED("전액 환불 완료");

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
