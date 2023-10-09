package com.uriel.travel.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.uriel.travel.domain.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

public class EditorResponseDto {

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Create {

        Long postId;

        public static Create of(Posts posts) {
            return Create.builder()
                    .postId(posts.getId())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Update {

        Long postId;

        public static Update of(Posts posts) {
            return Update.builder()
                    .postId(posts.getId())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetPost {

        Long postId;
        String title;
        String content;

        public static GetPost of(Posts posts) {
            return GetPost.builder()
                    .postId(posts.getId())
                    .title(posts.getTitle())
                    .content(posts.getContent())
                    .build();
        }
    }
}
