package com.uriel.travel.service;

import com.uriel.travel.domain.Country;
import com.uriel.travel.domain.SaveState;
import com.uriel.travel.domain.dto.ImageDto;
import com.uriel.travel.domain.dto.travelPackage.PackageFilter;
import com.uriel.travel.domain.dto.travelPackage.PackageRequestDto;
import com.uriel.travel.domain.dto.travelPackage.PackageResponseDto;
import com.uriel.travel.domain.dto.tag.TagResponseDto;
import com.uriel.travel.domain.entity.Package;
import com.uriel.travel.domain.entity.*;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PackageService {

    private final EntityManager entityManager;

    private final PackageRepository packageRepository;
    private final PackageRepositoryCustomImpl packageRepositoryCustom;

    private final ThumbnailRepository thumbnailRepository;
    private final ScheduleRepository scheduleRepository;
    private final TaggingRepository taggingRepository;

    private final S3Service s3Service;

    // 패키지 임시 저장
    public Long temporarySave(PackageRequestDto.Create requestDto) {
        Package aPackage = requestDto.toEntity();
        aPackage.setSaveState(SaveState.TEMPORARY);
        aPackage.setPrivacy(requestDto.getPrivacy());
        return packageRepository.save(aPackage).getId();
    }

    // 패키지 저장
    public Long create(PackageRequestDto.Create requestDto) {
        Package aPackage = requestDto.toEntity();
        aPackage.setPrivacy(requestDto.getPrivacy());
        aPackage.setSaveState(SaveState.SAVED);
        return packageRepository.save(aPackage).getId();
    }

    // 패키지 수정
    public void update(PackageRequestDto.Update requestDto, Long packageId) {
        Package packageById = getPackageByPackageId(packageId);

        packageById.setPrivacy(requestDto.getPrivacy());
        packageById.update(requestDto);
    }

    // 패키지 수정
    public void changeToSaveState(PackageRequestDto.Update requestDto, Long packageId) {
        Package packageById = getPackageByPackageId(packageId);

        packageById.setPrivacy(requestDto.getPrivacy());
        packageById.setSaveState(SaveState.SAVED);
        packageById.update(requestDto);
    }

    // 패키지 삭제
    public void delete(List<Long> ids) {
        ids.forEach(id -> {
            Package aPackage = getPackageByPackageId(id);
            packageRepository.delete(aPackage);
        });
    }

    // 패키지 복사
    public void duplicatePackage(Long packageId) {

        Package aPackage = getPackageByPackageId(packageId);
        List<Thumbnail> thumbnailList = aPackage.getThumbnailList();
        List<Schedule> scheduleList = aPackage.getScheduleList();
        List<Tagging> taggingList = aPackage.getTaggingList();

        // 엔티티 새로 생성
        Package newPackage = aPackage.duplicate(aPackage);
        packageRepository.save(newPackage);

        // 썸네일
        thumbnailList
                .forEach(thumbnail -> {
                    Thumbnail newThumbnail = s3Service.duplicateThumbnail(thumbnail);
                    newThumbnail.setPackage(newPackage);
                    thumbnailRepository.save(newThumbnail);
                }
        );

        // 일정
        scheduleList
                .forEach(schedule -> {
                    entityManager.detach(schedule);
                    schedule.idInit();
                    schedule.setPackage(newPackage);
                    scheduleRepository.save(schedule);
                }
        );

        // 태깅
        taggingList
                .forEach(tagging -> {
                    entityManager.detach(tagging);
                    tagging.idInit();
                    tagging.setPackage(newPackage);
                    taggingRepository.save(tagging);
                }
        );
    }

    // 패키지 공개/비공개 처리
    public void privacyUpdate(String privacy, List<Long> ids) {
        ids.forEach(id -> {
            Package aPackage = getPackageByPackageId(id);

            aPackage.setPrivacy(privacy);
        });
    }

    // 패키지 상세 조회 (수정)
    @Transactional(readOnly = true)
    public PackageResponseDto.PackageInfo getPackageById(Long packageId) {
        // 패키지 기본 정보
        Package packageById = getPackageByPackageId(packageId);

        PackageResponseDto.PackageInfo packageInfo = PackageResponseDto.PackageInfo.of(packageById, "edit");

        packageInfo.setPrice(getMinPrice(packageById));
        packageInfo.setThumbnailList(getThumbnailList(packageById));

        // 일정
        List<PackageResponseDto.ScheduleResponseDto> scheduleList = new ArrayList<>();
        for (Schedule schedule : packageById.getScheduleList()) {
            scheduleList.add(PackageResponseDto.ScheduleResponseDto.of(schedule, "edit"));
        }
        packageInfo.setScheduleList(scheduleList);

        // 체크된 태그 리스트
        List<TagResponseDto.TagInfo> checkedTagList = new ArrayList<>();
        for (Tagging tagging : packageById.getTaggingList()) {
            checkedTagList.add(TagResponseDto.TagInfo.builder()
                    .tagId(tagging.getTag().getId())
                    .tagContent(tagging.getTag().getTagContent()).build());
        }
        packageInfo.setCheckedTagList(checkedTagList);

        return packageInfo;
    }

    // 패키지 태그 검색
    @Transactional(readOnly = true)
    public List<PackageFilter.PackageFilterResponseDto> packageSearchByFilterCond(PackageFilter.PackageFilterCond filterCond) {
        List<PackageFilter.PackageFilterResponseDto> responseDtos = packageRepositoryCustom.searchPackageByFilter(filterCond);

        responseDtos.forEach(dto -> {

            dto.setThumbnailList(getThumbnailList(packageRepository.findById(dto.getPackageId())
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND))
            ));

            // 최저가 계산
            Package aPackage = packageRepository.findById(dto.getPackageId())
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            dto.setPrice(getMinPrice(aPackage));

        });
        return responseDtos;
    }

    public Page<PackageFilter.PackageFilterForAdminResponseDto> packageByCountryForAdmin(PackageFilter.PackageFilterCondForAdmin filterCond) {
        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), filterCond.getLimit());
        return packageRepositoryCustom.searchPackageForAdmin(filterCond, pageRequest);
    }


    // 전체 패키지 목록
    public List<PackageResponseDto.PackageInfo> getAllPackages() {
        // 패키지 기본 정보
        List<PackageResponseDto.PackageInfo> packageDtoList = new ArrayList<>();
        List<Package> packageList = packageRepository.findAllByIsPublic();

        for (Package aPackage : packageList) {
            packageDtoList.add(changeToDto(aPackage, "detail"));
        }

        return packageDtoList;
    }

    // 여행지로 패키지 목록 조회
    public List<PackageResponseDto.PackageInfo> getPackagesByCountry(String countryName) {

        List<PackageResponseDto.PackageInfo> packageDtoList = new ArrayList<>();
        List<Package> packageList = packageRepository.findByCountry(Country.from(countryName));

        for (Package aPackage : packageList) {
            PackageResponseDto.PackageInfo dto = changeToDto(aPackage, "detail");
            dto.setPrice(getMinPrice(aPackage));
            dto.setThumbnailList(getThumbnailList(aPackage));
            packageDtoList.add(dto);
        }

        return packageDtoList;
    }

    private PackageResponseDto.PackageInfo changeToDto(Package aPackage, String operation) {
        PackageResponseDto.PackageInfo packageInfo = PackageResponseDto.PackageInfo.of(aPackage, operation);

        packageInfo.setPrice(getMinPrice(aPackage));
        packageInfo.setThumbnailList(getThumbnailList(aPackage));

        return packageInfo;
    }

    private int getMinPrice(Package aPackage) {
        // 최저가 계산
        int minPrice = 0;
        for (Product product : aPackage.getProductList()) {
            if (product.getStartDate().isAfter(LocalDateTime.now())) {
                if (minPrice == 0) {
                    minPrice = product.getPrice();
                } else if (product.getPrice() <= minPrice) {
                    minPrice = product.getPrice();
                }
            }
        }
        return minPrice;
    }

    private List<ImageDto> getThumbnailList(Package aPackage) {

        List<ImageDto> imageDtos = new ArrayList<>();
        for (Thumbnail thumbnail : aPackage.getThumbnailList()) {
            imageDtos.add(ImageDto.builder()
                    .imageUrl(thumbnail.getImageUrl())
                    .uploadImageName(thumbnail.getUploadImageName())
                    .imagePath(thumbnail.getImagePath())
                    .originalImageName(thumbnail.getOriginalImageName())
                    .build());
        }
        return imageDtos;
    }

    private Package getPackageByPackageId(Long packageId) {
        return packageRepository.findById(packageId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
    }

    // 전체 패키지 목록
    public List<PackageResponseDto.PackageInfoSimple> getAllPackagesSimple() {
        return packageRepository.findAllPackageNames()
                .stream()
                .map(PackageResponseDto.PackageInfoSimple::new)
                .collect(Collectors.toList());
    }
}
