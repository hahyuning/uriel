package com.uriel.travel.repository;

import com.uriel.travel.dto.PackageFilterResponseDto;
import com.uriel.travel.dto.PackageRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PackageRepositoryCustom {

    Page<PackageFilterResponseDto> searchPackageByFilter(PackageRequestDto.FilterCond filterCond, Pageable pageable);
}
