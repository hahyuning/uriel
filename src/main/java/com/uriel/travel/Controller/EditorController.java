package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.PostType;
import com.uriel.travel.dto.editor.EditorRequestDto;
import com.uriel.travel.dto.editor.EditorResponseDto;
import com.uriel.travel.service.EditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class EditorController {

    private final EditorService editorService;

    // 게시글 등록
    @PostMapping("")
    public BaseResponse<EditorResponseDto.Create> create(@RequestBody EditorRequestDto.Create requestDto) {
        return BaseResponse.ok(editorService.create(requestDto));
    }

    // 게시글 수정
    // TODO: 게시글과 관련된 이미지 S3에서도 삭제
    @PutMapping("/{postId}")
    public BaseResponse<EditorResponseDto.Update> update(@RequestBody EditorRequestDto.Update requestDto,
                                                         @PathVariable Long postId) {
        return BaseResponse.ok(editorService.update(requestDto, postId));
    }

    // 게시글 삭제
    // TODO: 게시글과 관련된 이미지 S3에서도 삭제
    @DeleteMapping("/{postId}")
    public BaseResponse<Void> delete(@PathVariable Long postId) {
        editorService.delete(postId);
        return BaseResponse.ok();
    }

    // 게시글 한건 조회
    @GetMapping("/{postId}")
    public BaseResponse<EditorResponseDto.GetPost> update(@PathVariable Long postId) {
        return BaseResponse.ok(editorService.getPost(postId));
    }

    // 공지사항 목록 조회
    @GetMapping("/notice/{offset}")
    public BaseResponse<List<EditorResponseDto.GetPost>> getNotice(@PathVariable int offset) {
        return BaseResponse.ok(editorService.getPostByPostType(PostType.NOTICE, offset));
    }

    // 여행이야기 목록 조회
    @GetMapping("/blog/{offset}")
    public BaseResponse<List<EditorResponseDto.GetPost>> getBlog(@PathVariable int offset) {
        return BaseResponse.ok(editorService.getPostByPostType(PostType.BLOG, offset));
    }
}
