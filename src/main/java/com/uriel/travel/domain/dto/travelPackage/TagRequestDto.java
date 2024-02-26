package com.uriel.travel.domain.dto.travelPackage;

import com.uriel.travel.domain.entity.Tag;
import com.uriel.travel.domain.TagType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

public class TagRequestDto {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {

        Long tagId;

        String tagType;
        String tagContent;

        public Tag toEntity() {
            return Tag.builder()
                    .tagType(TagType.from(tagType))
                    .tagContent(tagContent)
                    .build();
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {

        Long tagId;

        String tagType;
        String tagContent;
    }
}
