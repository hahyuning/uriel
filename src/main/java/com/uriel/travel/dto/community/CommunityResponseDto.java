package com.uriel.travel.dto.community;

import com.uriel.travel.domain.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

public class CommunityResponseDto {

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class GetPostForUpdate {

        Long postId;
        String title;
        String content;
        LocalDate createdDate;

        public static GetPostForUpdate of(Posts posts) {
            return GetPostForUpdate.builder()
                    .postId(posts.getId())
                    .title(posts.getTitle())
                    .content(posts.getContentMd())
                    .createdDate(posts.getCreatedDate().toLocalDate())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class GetPostDetail {

        Long postId;
        String title;
        String content;
        LocalDate createdDate;

        public static GetPostDetail of(Posts posts) {
            return GetPostDetail.builder()
                    .postId(posts.getId())
                    .title(posts.getTitle())
                    .content(posts.getContentHtml())
                    .createdDate(posts.getCreatedDate().toLocalDate())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class GetBlogForMain {

        Long blogId;
        String title;
        String blogUrl;
        LocalDate createdDate;

        public static GetBlogForMain of(Posts posts) {
            return GetBlogForMain.builder()
                    .blogId(posts.getId())
                    .title(posts.getTitle())
                    .blogUrl(posts.getBlogUrl())
                    .createdDate(posts.getCreatedDate().toLocalDate())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class GetPostList {

        Long postId;
        String title;
        LocalDate createdDate;

        public static GetPostList of(Posts posts) {
            return GetPostList.builder()
                    .postId(posts.getId())
                    .title(posts.getTitle())
                    .createdDate(posts.getCreatedDate().toLocalDate())
                    .build();
        }
    }
}
