package com.uriel.travel.service;

import com.uriel.travel.domain.entity.Package;
import com.uriel.travel.domain.entity.Schedule;
import com.uriel.travel.domain.dto.travelPackage.PackageRequestDto;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.PackageRepository;
import com.uriel.travel.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PackageRepository packageRepository;

    // 일정 등록
    public void create(List<PackageRequestDto.ScheduleRequestDto> scheduleDtoList, Long packageId) {

        scheduleDtoList.forEach(scheduleDto -> {
            Schedule schedule = scheduleRepository.save(scheduleDto.toEntity());

            schedule.setPackage(packageRepository.findById(packageId)
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND)));
        });
    }

    // 일정 삭제
    public void deleteAllSchedule(Long packageId) {
        Package aPackage  = packageRepository.findById(packageId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        List<Schedule> allByPackageId = scheduleRepository.findAllByPackageId(packageId);
        allByPackageId.forEach(schedule -> {
            aPackage.getScheduleList().remove(schedule);
        });
    }
}
