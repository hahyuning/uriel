package com.uriel.travel.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.uriel.travel.domain.PostType;
import com.uriel.travel.domain.Posts;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

public class EditorRequestDto {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Create {

        String type;

        String title;
        String content;

        public Posts toEntity() {
            if (type.equals("blog")) {
                return Posts.builder()
                        .postType(PostType.BLOG)
                        .title(title)
                        .content(content)
                        .build();
            } else {
                return Posts.builder()
                        .postType(PostType.NOTICE)
                        .title(title)
                        .content(content)
                        .build();
            }
        }

    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Update {

        String type;

        String title;
        String content;
    }
}
