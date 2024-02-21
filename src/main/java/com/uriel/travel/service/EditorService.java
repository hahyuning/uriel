package com.uriel.travel.service;

import com.uriel.travel.domain.PostType;
import com.uriel.travel.domain.Posts;
import com.uriel.travel.dto.editor.EditorRequestDto;
import com.uriel.travel.dto.editor.EditorResponseDto;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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
    public EditorResponseDto.Create create(EditorRequestDto.Create requestDto) {
        return EditorResponseDto.Create.of(postsRepository.save(requestDto.toEntity()));
    }

    // 게시글 수정
    public EditorResponseDto.Update update(EditorRequestDto.Update requestDto, Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        post.update(requestDto);

        return EditorResponseDto.Update.of(post);
    }

    // 게시글 삭제
    public void delete(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        postsRepository.delete(post);
    }

    // 게시글 한건 조회
    @Transactional(readOnly = true)
    public EditorResponseDto.GetPost getPost(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        return EditorResponseDto.GetPost.of(post);
    }

    // 게시글 목록 조회 (공지사항, 블로그)
    @Transactional(readOnly = true)
    public List<EditorResponseDto.GetPost> getPostByPostType(PostType postType, int offset) {

        PageRequest pageRequest = PageRequest.of(offset, 10);

        List<Posts> allByPostType = postsRepository.findAllByPostType(postType, pageRequest);
        List<EditorResponseDto.GetPost> postList = new ArrayList<>();

        allByPostType.forEach(posts -> {
            postList.add(EditorResponseDto.GetPost.of(posts));
        });
        return postList;
    }
}
