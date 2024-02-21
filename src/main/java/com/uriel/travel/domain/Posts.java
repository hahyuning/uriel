package com.uriel.travel.domain;

import com.uriel.travel.dto.community.CommunityRequestDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posts_id")
    Long id;

    String title;

    @Lob
    String contentHtml;

    @Lob
    String contentMd;

    @Enumerated(EnumType.STRING)
    PostType postType;

    String blogUrl;

    public void update(CommunityRequestDto.Update requestDto) {
        this.title = requestDto.getTitle();
        this.contentHtml = requestDto.getContentHtml();
        this.contentMd = requestDto.getContentMd();

        if (requestDto.getType().equals("blog")) {
            this.blogUrl = requestDto.getBlogUrl();
        }
    }
}
