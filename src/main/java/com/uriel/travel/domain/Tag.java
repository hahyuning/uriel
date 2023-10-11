package com.uriel.travel.domain;
import com.uriel.travel.domain.TagType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

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
