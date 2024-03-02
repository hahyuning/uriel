package com.uriel.travel.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uriel.travel.domain.TagType;
import com.uriel.travel.domain.dto.tag.TagRequestDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    Long id;

    String tagContent;

    @Enumerated(EnumType.STRING)
    TagType tagType;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Tagging> taggingList = new ArrayList<>();

    public void update(TagRequestDto.Update requestDto) {
        this.tagContent = requestDto.getTagContent();
        this.tagType = TagType.from(requestDto.getTagType());
    }
}
