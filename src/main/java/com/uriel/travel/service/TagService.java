package com.uriel.travel.service;

import com.uriel.travel.domain.Package;
import com.uriel.travel.domain.Tag;
import com.uriel.travel.domain.TagType;
import com.uriel.travel.domain.Tagging;
import com.uriel.travel.dto.PackageResponseDto;
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

    // 패키지에 태깅
    public void taggingToPackage(List<String> tagList, Long packageId) {
        Package aPackage = packageRepository.findById(packageId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        tagList.forEach(tagContent -> {
            log.info(tagContent);
            Tag tag = tagRepository.findByTagContent(tagContent);
            Tagging tagging = new Tagging(tag);
            taggingRepository.save(tagging);
            tagging.setPackage(aPackage);
        });
    }

    // 패키지 삭제 시 태그 삭제
    public void deleteTagging(Long packageId) {
        taggingRepository.deleteAll(taggingRepository.findAllByPackageId(packageId));
    }

    // 태그 전체 조회
    @Transactional(readOnly = true)
    public PackageResponseDto.GetAllTags getAllTags() {

        List<String> themeList = tagRepository.findByTagType(TagType.THEME);
        List<String> familyList = tagRepository.findByTagType(TagType.FAMILY);
        List<String> seasonList = tagRepository.findByTagType(TagType.SEASON);
        return PackageResponseDto.GetAllTags.of(themeList, familyList, seasonList);
    }
}
