package com.uriel.travel.domain.dto.community;

import com.uriel.travel.domain.PostType;
import com.uriel.travel.domain.entity.Posts;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

public class CommunityRequestDto {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create { // html, md 같이 저장

        String type;
        String title;
        String contentHtml;
        String contentMd;
        String blogUrl;


        public Posts toEntity() {
            if (type.equals("여행이야기")) {
                return Posts.builder()
                        .postType(PostType.BLOG)
                        .title(title)
                        .contentHtml(contentHtml)
                        .contentMd(contentMd)
                        .blogUrl(blogUrl)
                        .build();
            } else {
                return Posts.builder()
                        .postType(PostType.from(type))
                        .title(title)
                        .contentHtml(contentHtml)
                        .contentMd(contentMd)
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
        String contentHtml;
        String contentMd;
        String blogUrl;
    }
}
