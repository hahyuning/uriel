package com.uriel.travel.domain.entity;

import com.uriel.travel.domain.PostType;
import com.uriel.travel.domain.dto.community.CommunityRequestDto;
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

    @Column(columnDefinition = "LONGTEXT")
    String contentHtml;

    @Column(columnDefinition = "LONGTEXT")
    String contentMd;

    @Enumerated(EnumType.STRING)
    PostType postType;

    String blogUrl;

    public void update(CommunityRequestDto.Update requestDto) {
        this.title = requestDto.getTitle();
        this.contentHtml = requestDto.getContentHtml();
        this.contentMd = requestDto.getContentMd();

        if (requestDto.getType().equals("여행이야기")) {
            this.blogUrl = requestDto.getBlogUrl();
        }
    }
}
