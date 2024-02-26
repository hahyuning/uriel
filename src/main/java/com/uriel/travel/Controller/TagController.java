package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.product.TagRequestDto;
import com.uriel.travel.domain.dto.product.TagResponseDto;
import com.uriel.travel.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    // 전체 태그 조회
    @GetMapping
    public BaseResponse<TagResponseDto.GetAllTags> getAllTags() {
        return BaseResponse.ok(tagService.getAllTags());
    }

    // 태그 등록
    @PostMapping("/create")
    public BaseResponse<Void> create(@RequestBody TagRequestDto.Create requestDto) {
        tagService.create(requestDto);
        return BaseResponse.ok();
    }

    // 태그 수정
    @PutMapping("/update")
    public BaseResponse<Void> create(@RequestBody TagRequestDto.Update requestDto) {
        tagService.update(requestDto);
        return BaseResponse.ok();
    }

    // 태그 삭제
    @DeleteMapping("/{tagId}")
    public BaseResponse<Void> create(@PathVariable Long tagId) {
        tagService.delete(tagId);
        return BaseResponse.ok();
    }
}
