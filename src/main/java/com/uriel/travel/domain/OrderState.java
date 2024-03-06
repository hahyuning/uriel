package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderState implements CodedEnum<String> {

    READY("결제 대기"),
    RESERVED("예약금 완료"),
    COMPLETED("잔금 완료"),
    PAYMENT_NEEDED("추가 결제 필요"),
    REFUND_NEEDED("환불 필요"),
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
