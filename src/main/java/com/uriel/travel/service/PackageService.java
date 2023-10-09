package com.uriel.travel.service;

import com.uriel.travel.domain.Package;
import com.uriel.travel.dto.PackageRequestDto;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PackageService {

    private final PackageRepository packageRepository;

    // 패키지 등록
    public void create(PackageRequestDto.Create requestDto) {
        packageRepository.save(requestDto.toEntity());
    }

    // 패키지 수정
    public void create(PackageRequestDto.Update requestDto) {
        packageRepository.save(requestDto.toEntity());
    }

    // 패키지 삭제
    public void delete(List<Long> packageIds) {
        packageIds.forEach(id -> {
            Package aPackage = packageRepository.findById(id)
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));
            packageRepository.delete(aPackage);
        });
    }
}