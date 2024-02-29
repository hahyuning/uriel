package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.BatchRequestDto;
import com.uriel.travel.domain.dto.filterCond.PackageFilter;
import com.uriel.travel.domain.dto.travelPackage.PackageRequestDto;
import com.uriel.travel.domain.dto.travelPackage.PackageResponseDto;
import com.uriel.travel.service.PackageService;
import com.uriel.travel.service.S3Service;
import com.uriel.travel.service.ScheduleService;
import com.uriel.travel.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/packages")
public class PackageController {

    private final PackageService packageService;
    private final S3Service s3Service;
    private final ScheduleService scheduleService;
    private final TagService tagService;
    // 패키지 등록
    @PostMapping("/create")
    public BaseResponse<Void> create(@RequestPart("data") PackageRequestDto.Create requestDto,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        Long packageId = packageService.create(requestDto); // 패키지 저장
        if (files != null) {
            s3Service.uploadThumbnails(files, packageId); // 썸네일 저장
        }

        // 일정 저장
        scheduleService.create(requestDto.getScheduleList(), packageId);

        // 태그 저장
        tagService.taggingToPackage(requestDto.getThemeList(), packageId);
        tagService.taggingToPackage(requestDto.getFamilyList(), packageId);
        tagService.taggingToPackage(requestDto.getSeasonList(), packageId);
        tagService.taggingToPackage(requestDto.getPriceList(), packageId);
        return BaseResponse.ok();
    }

    // 패키지 임시저장
    @PostMapping("/temp-create")
    public BaseResponse<Void> temporarySave(@RequestPart("data") PackageRequestDto.Create requestDto,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        Long packageId = packageService.temporarySave(requestDto); // 패키지 저장
        s3Service.uploadThumbnails(files, packageId); // 썸네일 저장

        // 일정 저장
        scheduleService.create(requestDto.getScheduleList(), packageId);

        // 태그 저장
        tagService.taggingToPackage(requestDto.getThemeList(), packageId);
        tagService.taggingToPackage(requestDto.getFamilyList(), packageId);
        tagService.taggingToPackage(requestDto.getSeasonList(), packageId);
        tagService.taggingToPackage(requestDto.getPriceList(), packageId);
        return BaseResponse.ok();
    }

    // 패키지 수정
    @PutMapping("/{packageId}")
    public BaseResponse<Void> update(@RequestPart("data") PackageRequestDto.Update requestDto,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                     @PathVariable Long packageId) {

        // 기본정보 업데이트
        packageService.update(requestDto, packageId);
        // 썸네일 업데이트
        s3Service.deleteThumbnail(packageId);
        s3Service.uploadThumbnails(files, packageId);

        //일정 업데이트
        scheduleService.deleteAllSchedule(packageId);
        scheduleService.create(requestDto.getScheduleList(), packageId);

        // 태그 업데이트
        tagService.deleteTagging(packageId);
        tagService.taggingToPackage(requestDto.getThemeList(), packageId);
        tagService.taggingToPackage(requestDto.getFamilyList(), packageId);
        tagService.taggingToPackage(requestDto.getSeasonList(), packageId);
        tagService.taggingToPackage(requestDto.getPriceList(), packageId);
        return BaseResponse.ok();
    }

    // 패키지 임시저장 -> 저장
    @PutMapping("/save/{packageId}")
    public BaseResponse<Void> changeToSaveState(@RequestPart("data") PackageRequestDto.Update requestDto,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                     @PathVariable Long packageId) {

        // 기본정보 업데이트
        packageService.changeToSaveState(requestDto, packageId);

        // 썸네일 업데이트
        s3Service.deleteThumbnail(packageId);
        s3Service.uploadThumbnails(files, packageId);

        //일정 업데이트
        scheduleService.deleteAllSchedule(packageId);
        scheduleService.create(requestDto.getScheduleList(), packageId);

        // 태그 업데이트
        tagService.deleteTagging(packageId);
        tagService.taggingToPackage(requestDto.getThemeList(), packageId);
        tagService.taggingToPackage(requestDto.getFamilyList(), packageId);
        tagService.taggingToPackage(requestDto.getSeasonList(), packageId);
        tagService.taggingToPackage(requestDto.getPriceList(), packageId);
        return BaseResponse.ok();
    }

    // 패키지 삭제
    @PostMapping("/batch-delete")
    public BaseResponse<Void> delete(@RequestBody BatchRequestDto requestDto) {
        List<Long> ids = requestDto.getIds();

        // 태그 삭제
        ids.forEach(tagService::deleteTagging);

        // 썸네일 삭제
        ids.forEach(s3Service::deleteThumbnail);

        packageService.delete(ids);
        return BaseResponse.ok();
    }

    // 패키지 공개/비공개 처리
    @PostMapping("/batch-update")
    public BaseResponse<Void> privacyUpdate(@RequestBody BatchRequestDto requestDto) {
        packageService.privacyUpdate(requestDto.getOperation(), requestDto.getIds());
        return BaseResponse.ok();
    }

    // 패키지 한건 조회
    @GetMapping("/{packageId}")
    public BaseResponse<PackageResponseDto.PackageInfo> getPackageById(@PathVariable Long packageId) {
        return BaseResponse.ok(packageService.getPackageById(packageId));
    }

    // 패키지 태그 검색
    @PostMapping("/tags")
    public BaseResponse<List<PackageFilter.PackageFilterResponseDto>> packageSearch(@RequestBody PackageFilter.PackageFilterCond filterCond) {
        return BaseResponse.ok(packageService.packageSearchByFilterCond(filterCond));
    }

    // 관리자용 패키지 목록 조회
    @PostMapping
    public BaseResponse<Page<PackageFilter.PackageFilterForAdminResponseDto>> packageSearchForAdmin(@RequestBody PackageFilter.PackageFilterCondForAdmin filterCond) {
        return BaseResponse.ok(packageService.packageByCountryForAdmin(filterCond));
    }

    // 패키지 전체 조회
    @GetMapping
    public BaseResponse<List<PackageResponseDto.PackageInfo>> getAllPackages() {
        return BaseResponse.ok(packageService.getAllPackages());
    }

    // 여행지별 패키지 목록 조회
    @GetMapping("/countries/{countryName}")
    public BaseResponse<List<PackageResponseDto.PackageInfo>> getPackageByCountryName(@PathVariable String countryName) {
        return BaseResponse.ok(packageService.getPackagesByCountry(countryName));
    }

    // 패키지 복사
    @GetMapping("/duplicate/{packageId}")
    public BaseResponse<Void> duplicatePackage(@PathVariable Long packageId) {
        packageService.duplicatePackage(packageId);
        return BaseResponse.ok();
    }
}
