package com.uriel.travel.dto.editor;

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
    public static class Create {

        String type;

        String title;
        String content;
        String blogUrl;

        public Posts toEntity() {
            if (type.equals("blog")) {
                return Posts.builder()
                        .postType(PostType.BLOG)
                        .title(title)
                        .content(content)
                        .blogUrl(blogUrl)
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
    public static class Update {

        String type;

        String title;
        String content;
        String blogUrl;
    }
}
