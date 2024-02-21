package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PostType {
    NOTICE("공지사항"), BLOG("여행이야기");

    private final String viewName;

    @JsonCreator
    public static PostType from(String sub) {
        for (PostType postType : PostType.values()) {
            if (postType.getViewName().equals(sub)) {
                return postType;
            }
        }
        return null;
    }
}
