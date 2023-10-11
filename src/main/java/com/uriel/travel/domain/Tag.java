package com.uriel.travel.domain;

import com.uriel.travel.domain.enumeration.TagType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
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

    @JsonIgnore
    @OneToMany(mappedBy = "tag")
    List<Tagging> taggingList = new ArrayList<>();
}
