package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender implements CodedEnum<String> {

    MALE("남"),
    FEMALE("여"),
    OTHER("기타");

    private final String viewName;

    @JsonCreator
    public static Country from(String sub) {
        for (Country country : Country.values()) {
            if (country.getViewName().equals(sub)) {
                return country;
            }
        }
        return null;
    }

    public static Gender mapToGender(String genderString) {
        if ("M".equalsIgnoreCase(genderString)) {
            return Gender.MALE;
        } else if ("F".equalsIgnoreCase(genderString)) {
            return Gender.FEMALE;
        } else {
            return Gender.OTHER;
        }
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<Country, String> {
        public Converter() {
            super(Country.class);
        }
    }
}
