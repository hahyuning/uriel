package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductState {
    RESERVATION_AVAILABLE("예약 가능"),
    RESERVATION_DEADLINE("예약 마감");

    private final String viewName;

    @JsonCreator
    public static ProductState from(String sub) {
        for (ProductState state : ProductState.values()) {
            if (state.getViewName().equals(sub)) {
                return state;
            }
        }
        return null;
    }
}
