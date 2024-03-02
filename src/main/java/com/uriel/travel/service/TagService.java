package com.uriel.travel.service;

import com.uriel.travel.domain.entity.Package;
import com.uriel.travel.domain.entity.Tag;
import com.uriel.travel.domain.TagType;
import com.uriel.travel.domain.entity.Tagging;
import com.uriel.travel.domain.dto.tag.TagRequestDto;
import com.uriel.travel.domain.dto.tag.TagResponseDto;
import com.uriel.travel.exception.CustomBadRequestException;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.PackageRepository;
import com.uriel.travel.repository.TagRepository;
import com.uriel.travel.repository.TaggingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TagService {

    private final TaggingRepository taggingRepository;
    private final TagRepository tagRepository;
    private final PackageRepository packageRepository;

    // 태그 등록
    public void create(TagRequestDto.Create requestDto) {

        // 태그 중복 확인
        tagRepository.findByTagContent(requestDto.getTagContent())
                        .ifPresent(tag -> {
                            throw new CustomBadRequestException(ErrorCode.DUPLICATE_TAG);
                        });

        tagRepository.save(requestDto.toEntity());
    }

    // 태그 수정
    public void update(TagRequestDto.Update requestDto) {
        Tag tag = tagRepository.findById(requestDto.getTagId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        tag.update(requestDto);
    }

    // 태그 삭제
    public void delete(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        tagRepository.delete(tag);
    }

    // 패키지에 태깅
    public void taggingToPackage(List<String> tagList, Long packageId) {
        Package aPackage = packageRepository.findById(packageId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        tagList.forEach(tagContent -> {
            Tag tag = tagRepository.findByTagContent(tagContent)
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND_TAG));
            Tagging tagging = new Tagging();
            tagging.setTag(tag);
            tagging.setPackage(aPackage);
            taggingRepository.save(tagging);
        });
    }

    public void deleteTagging(Long packageId) {
        taggingRepository.deleteAll(taggingRepository.findAllByPackageId(packageId));
    }

    // 태그 전체 조회
    @Transactional(readOnly = true)
    public TagResponseDto.GetAllTags getAllTags() {

        List<Tag> themeList = tagRepository.findByTagType(TagType.THEME.toString());
        List<Tag> familyList = tagRepository.findByTagType(TagType.FAMILY.toString());
        List<Tag> seasonList = tagRepository.findByTagType(TagType.SEASON.toString());
        List<Tag> priceList = tagRepository.findByTagType(TagType.PRICE.toString());
        return TagResponseDto.GetAllTags.of(themeList, familyList, seasonList, priceList);
    }
}
