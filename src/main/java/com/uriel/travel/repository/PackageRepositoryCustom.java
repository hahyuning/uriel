package com.uriel.travel.repository;

import com.uriel.travel.dto.PackageFilterResponseDto;
import com.uriel.travel.dto.PackageRequestDto;

import java.util.List;

public interface PackageRepositoryCustom {

    List<PackageFilterResponseDto> searchPackageByFilter(PackageRequestDto.FilterCond filterCond);
}
