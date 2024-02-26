package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.PostType;
import com.uriel.travel.domain.dto.community.CommunityRequestDto;
import com.uriel.travel.domain.dto.community.CommunityResponseDto;
import com.uriel.travel.service.EditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommunityController {

    private final EditorService editorService;

    // 게시글 등록
    @PostMapping
    public BaseResponse<CommunityResponseDto.GetPostDetail> create(@RequestBody CommunityRequestDto.Create requestDto) {
        return BaseResponse.ok(editorService.create(requestDto));
    }

    // 게시글 수정
    // TODO: 게시글과 관련된 이미지 S3에서도 삭제
    @PutMapping("/{postId}")
    public BaseResponse<CommunityResponseDto.GetPostDetail> update(@RequestBody CommunityRequestDto.Update requestDto,
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

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public BaseResponse<CommunityResponseDto.GetPostDetail> getPostDetail(@PathVariable Long postId) {
        return BaseResponse.ok(editorService.getPostDetail(postId));
    }

    // 게시글 수정폼
    @GetMapping("/update/{postId}")
    public BaseResponse<CommunityResponseDto.GetPostForUpdate> getPosForUpdaet(@PathVariable Long postId) {
        return BaseResponse.ok(editorService.getPostForUpdate(postId));
    }

    // 게시글 목록 조회
    @GetMapping("/{postType}/{offset}")
    public BaseResponse<List<CommunityResponseDto.GetPostList>> getNotice(@PathVariable String postType, @PathVariable int offset) {
        return BaseResponse.ok(editorService.getPostByPostType(PostType.from(postType), offset));
    }

    // 여행이야기 목록 조회 (메인)
    @GetMapping("/blog")
    public BaseResponse<List<CommunityResponseDto.GetBlogForMain>> getBlogForMain() {
        return BaseResponse.ok(editorService.getBlogForMain());
    }
}
