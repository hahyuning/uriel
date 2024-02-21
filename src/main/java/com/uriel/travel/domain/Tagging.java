package com.uriel.travel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tagging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    Package aPackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    Tag tag;

    @Enumerated(EnumType.STRING)
    TagType tagType;

    public void setTag(Tag tag) {
        this.tag = tag;
        tag.getTaggingList().add(this);
    }

    public void setPackage(Package aPackage) {
        this.aPackage = aPackage;
        aPackage.getTaggingList().add(this);
    }
}