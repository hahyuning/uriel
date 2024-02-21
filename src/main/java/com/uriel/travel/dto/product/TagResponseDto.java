package com.uriel.travel.dto.product;

import com.uriel.travel.domain.Tag;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

public class TagResponseDto {

    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class GetAllTags {

        List<TagInfo> themeList;
        List<TagInfo> familyList;
        List<TagInfo> seasonList;
        List<TagInfo> priceList;

        public static TagResponseDto.GetAllTags of(List<Tag> themeList,
                                                   List<Tag> familyList,
                                                   List<Tag> seasonList,
                                                   List<Tag> priceList) {

            return GetAllTags.builder()
                    .themeList(changeToDto(themeList))
                    .familyList(changeToDto(familyList))
                    .seasonList(changeToDto(seasonList))
                    .priceList(changeToDto(priceList))
                    .build();
        }

        private static List<TagInfo> changeToDto(List<Tag> entity) {
            List<TagInfo> result = new ArrayList<>();

            for (Tag tag : entity) {
                result.add(TagInfo.builder()
                        .tagId(tag.getId())
                        .tagContent(tag.getTagContent())
                        .build());
            }
            return result;
        }
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class TagInfo {

        Long tagId;
        String tagContent;
    }
}
