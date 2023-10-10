package com.uriel.travel.domain;

import com.querydsl.core.annotations.QueryInit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public Tagging(Tag tag) {
        this.tag = tag;
        this.tagType = tag.getTagType();
    }

    public void setPackage(Package aPackage) {
        this.aPackage = aPackage;
    }
}
