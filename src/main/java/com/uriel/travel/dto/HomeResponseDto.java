package com.uriel.travel.dto;

import com.uriel.travel.dto.editor.EditorResponseDto;
import com.uriel.travel.dto.product.TagResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeResponseDto {

    TagResponseDto.GetAllTags allTags;
    List<EditorResponseDto.GetPost> postList;

    public static HomeResponseDto of(TagResponseDto.GetAllTags allTags, List<EditorResponseDto.GetPost> postList) {
        return HomeResponseDto.builder()
                .allTags(allTags)
                .postList(postList)
                .build();
    }
}
