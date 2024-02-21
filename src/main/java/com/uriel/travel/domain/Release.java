package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Release {

    PUBLIC("공개"),
    PRIVATE("비공개"),
    TEMPORARY("임시저장");

    private final String viewName;

    @JsonCreator
    public static Release from(String sub) {
        for (Release release : Release.values()) {
            if (release.getViewName().equals(sub)) {
                return release;
            }
        }
        return null;
    }
}
