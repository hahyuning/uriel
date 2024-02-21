package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TagType {
    THEME("테마"),
    FAMILY("구성원"),
    SEASON("시기"),
    PRICE("비용");

    private final String viewName;

    @JsonCreator
    public static TagType from(String sub) {
        for (TagType tagType : TagType.values()) {
            if (tagType.getViewName().equals(sub)) {
                return tagType;
            }
        }
        return null;
    }
}