package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.PostType;
import com.uriel.travel.dto.HomeResponseDto;
import com.uriel.travel.service.EditorService;
import com.uriel.travel.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final TagService tagService;
    private final EditorService editorService;

    @GetMapping
    public BaseResponse<HomeResponseDto> getTagsAndBlogs() {
        return BaseResponse.ok(HomeResponseDto.of(tagService.getAllTags(), editorService.getPostByPostType(PostType.BLOG, 0)));
    }
}
