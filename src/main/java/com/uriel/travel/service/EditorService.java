package com.uriel.travel.service;

import com.uriel.travel.domain.PostType;
import com.uriel.travel.domain.Posts;
import com.uriel.travel.dto.community.CommunityRequestDto;
import com.uriel.travel.dto.community.CommunityResponseDto;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EditorService {

    private final PostsRepository postsRepository;

    // 게시글 등록
    public CommunityResponseDto.GetPostDetail create(CommunityRequestDto.Create requestDto) {
        return CommunityResponseDto.GetPostDetail.of(postsRepository.save(requestDto.toEntity()));
    }

    // 게시글 수정
    public CommunityResponseDto.GetPostDetail update(CommunityRequestDto.Update requestDto, Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        post.update(requestDto);

        return CommunityResponseDto.GetPostDetail.of(post);
    }

    // 게시글 삭제
    public void delete(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        postsRepository.delete(post);
    }

    // 게시글 상세조회
    @Transactional(readOnly = true)
    public CommunityResponseDto.GetPostDetail getPostDetail(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        return CommunityResponseDto.GetPostDetail.of(post);
    }

    // 게시글 수정폼
    @Transactional(readOnly = true)
    public CommunityResponseDto.GetPostForUpdate getPostForUpdate(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        return CommunityResponseDto.GetPostForUpdate.of(post);
    }

    // 게시글 목록 조회 (공지사항, 블로그)
    @Transactional(readOnly = true)
    public List<CommunityResponseDto.GetPostList> getPostByPostType(PostType postType, int offset) {

        PageRequest pageRequest = PageRequest.of(offset, 10, Sort.by("createdDate").descending());

        List<Posts> allByPostType = postsRepository.findAllByPostType(postType, pageRequest);
        List<CommunityResponseDto.GetPostList> postList = new ArrayList<>();

        allByPostType.forEach(posts -> {
            postList.add(CommunityResponseDto.GetPostList.of(posts));
        });
        return postList;
    }

    // 여행이야기 목록 조회 (메인)
    @Transactional(readOnly = true)
    public List<CommunityResponseDto.GetBlogForMain> getBlogForMain() {

        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("createdDate").descending());

        List<Posts> allBlogs = postsRepository.findAllByPostType(PostType.BLOG, pageRequest);
        List<CommunityResponseDto.GetBlogForMain> blogList = new ArrayList<>();

        allBlogs.forEach(blogs -> {
            blogList.add(CommunityResponseDto.GetBlogForMain.of(blogs));
        });
        return blogList;
    }
}
