package com.uriel.travel.domain;

import com.uriel.travel.dto.EditorRequestDto;
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
    String content;

    @Enumerated(EnumType.STRING)
    PostType postType;

    public void update(EditorRequestDto.Update requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
