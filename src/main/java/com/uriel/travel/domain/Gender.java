package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE("남"),
    FEMALE("여"),
    OTHER("기타");

    private final String viewName;

    @JsonCreator
    public static Gender from(String sub) {
        for (Gender gender : Gender.values()) {
            if (gender.getViewName().equals(sub)) {
                return gender;
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
}
