package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchType implements CodedEnum<String> {

    RESERVE_USER("예약자명"),
    PHONE_NUMBER("핸드폰"),
    EMAIL("이메일"),
    ORDER_NUMBER("주문번호");

    private final String viewName;

    @JsonCreator
    public static SearchType from(String sub) {
        for (SearchType type : SearchType.values()) {
            if (type.getViewName().equals(sub)) {
                return type;
            }
        }
        return null;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<SearchType, String> {
        public Converter() {
            super(SearchType.class);
        }
    }
}
