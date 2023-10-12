package com.uriel.travel.service;

import com.uriel.travel.domain.Package;
import com.uriel.travel.domain.Schedule;
import com.uriel.travel.domain.Thumbnail;
import com.uriel.travel.dto.*;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.PackageRepository;
import com.uriel.travel.repository.PackageRepositoryCustomImpl;
import com.uriel.travel.repository.ScheduleRepository;
import com.uriel.travel.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PackageService {

    private final PackageRepository packageRepository;
    private final PackageRepositoryCustomImpl packageRepositoryCustom;
    private final ThumbnailRepository thumbnailRepository;
    private final ScheduleRepository scheduleRepository;

    // 패키지 등록
    public Long create(PackageRequestDto.Create requestDto) {
        return packageRepository.save(requestDto.toEntity()).getId();
    }

    // 패키지 수정
    public void update(PackageRequestDto.Update requestDto, Long id) {
        Package packageById = packageRepository.findById(id)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        packageById.update(requestDto);
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

    // 패키지 한건 조회
    @Transactional(readOnly = true)
    public PackageResponseDto.GetPackage getPackageById(Long id) {
        Package packageById = packageRepository.findById(id)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        PackageResponseDto.GetPackage responseDto = PackageResponseDto.GetPackage.of(packageById);

        // 썸네일
        List<Thumbnail> thumbnails = thumbnailRepository.findAllByPackageId(id);
        List<ImageDto> thumbnailList = new ArrayList<>();
        thumbnails.forEach(thumbnail -> {
            thumbnailList.add(ImageDto.builder()
                    .originalImageName(thumbnail.getOriginalImageName())
                    .uploadImageName(thumbnail.getUploadImageName())
                    .imagePath(thumbnail.getImagePath())
                    .imageUrl(thumbnail.getImageUrl()).build());

        });
        responseDto.setThumbnailList(thumbnailList);

        // 일정
        List<Schedule> schedules = scheduleRepository.findAllByPackageId(id);
        List<ScheduleDto> scheduleList = new ArrayList<>();
        schedules.forEach(schedule -> {
            scheduleList.add(ScheduleDto.builder()
                    .scheduleId(schedule.getId())
                    .day(schedule.getDay())
                    .dayContent(schedule.getDayContent())
                    .hotel(schedule.getHotel())
                    .meal(schedule.getMeal())
                    .vehicle(schedule.getVehicle()).build());
        });

        responseDto.setScheduleList(scheduleList);
        return responseDto;
    }

    // 패키지 태그 검색
    @Transactional(readOnly = true)
    public List<PackageFilterResponseDto> packageSearchByFilterCond(PackageRequestDto.FilterCond filterCond) {
        List<PackageFilterResponseDto> packageFilterResponseDtos = packageRepositoryCustom.searchPackageByFilter(filterCond);
        packageFilterResponseDtos.forEach(dto -> {
            List<Thumbnail> thumbnails = thumbnailRepository.findAllByPackageId(dto.getPackageId());

            List<ImageDto> thumbnailList = new ArrayList<>();
            thumbnails.forEach(thumbnail -> {
                thumbnailList.add(ImageDto.builder()
                        .originalImageName(thumbnail.getOriginalImageName())
                        .uploadImageName(thumbnail.getUploadImageName())
                        .imagePath(thumbnail.getImagePath())
                        .imageUrl(thumbnail.getImageUrl()).build());

            });

            dto.setThumbnailList(thumbnailList);
        });
        return packageFilterResponseDtos;
    }
}
