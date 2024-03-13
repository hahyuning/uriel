package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    LOCAL("LOCAL"),
    NAVER("NAVER"),
    KAKAO("KAKAO");

    private final String viewName;

    @JsonCreator
    public static SocialType from(String sub) {
        for (SocialType type : SocialType.values()) {
            if (type.getViewName().equals(sub)) {
                return type;
            }
        }
        return null;
    }
}