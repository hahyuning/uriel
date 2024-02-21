package com.uriel.travel.service;

import com.uriel.travel.domain.Package;
import com.uriel.travel.domain.*;
import com.uriel.travel.dto.ImageDto;
import com.uriel.travel.dto.filterCond.PackageFilter;
import com.uriel.travel.dto.product.*;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PackageService {

    private final PackageRepository packageRepository;
    private final PackageRepositoryCustomImpl packageRepositoryCustom;

    // 패키지 임시 저장
    public Long temporarySave(PackageRequestDto.Create requestDto) {
        Package aPackage = requestDto.toEntity();
        return packageRepository.save(aPackage).getId();
    }

    // 패키지 저장
    public Long create(PackageRequestDto.Create requestDto) {
        Package aPackage = requestDto.toEntity();
        aPackage.setPrivacy(requestDto.getPrivacy());
        return packageRepository.save(aPackage).getId();
    }

    // 패키지 수정
    public void update(PackageRequestDto.Update requestDto, Long id) {
        Package packageById = packageRepository.findById(id)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        packageById.setPrivacy(requestDto.getPrivacy());
        packageById.update(requestDto);
    }

    // 패키지 임시저장 업데이트
    public void temporaryUpdate(PackageRequestDto.Update requestDto, Long id) {
        Package packageById = packageRepository.findById(id)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        packageById.update(requestDto);
        packageById.setPrivacy(Release.TEMPORARY.getViewName());
    }

    // 패키지 삭제
    public void delete(List<Long> ids) {
        ids.forEach(id -> {
            Package aPackage = packageRepository.findById(id)
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            packageRepository.delete(aPackage);
        });
    }

    // 패키지 공개/비공개 처리
    public void privacyUpdate(String privacy, List<Long> ids) {
        ids.forEach(id -> {
            Package aPackage = packageRepository.findById(id)
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            aPackage.setPrivacy(privacy);
        });
    }

    // 패키지 상세 조회 (수정)
    @Transactional(readOnly = true)
    public PackageResponseDto.PackageInfo getPackageById(Long packageId) {
        // 패키지 기본 정보
        Package packageById = packageRepository.findById(packageId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

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
    public Page<PackageFilter.PackageFilterResponseDto> packageSearchByFilterCond(PackageFilter.PackageFilterCond filterCond) {
        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), filterCond.getLimit());
        Page<PackageFilter.PackageFilterResponseDto> responseDtos = packageRepositoryCustom.searchPackageByFilter(filterCond, pageRequest);

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
        return packageRepositoryCustom.searchByCountryForAdmin(filterCond, pageRequest);
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
            packageDtoList.add(changeToDto(aPackage, "detail"));
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
            if (product.getStartDate().isAfter(LocalDateTime.now()) && product.getPrice() != 0 && product.getPrice() <= minPrice) {
                minPrice = product.getPrice();
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
}
