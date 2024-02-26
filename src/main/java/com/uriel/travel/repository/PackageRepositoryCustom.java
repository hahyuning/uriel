package com.uriel.travel.repository;

import com.uriel.travel.domain.dto.filterCond.PackageFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PackageRepositoryCustom {

    Page<PackageFilter.PackageFilterResponseDto> searchPackageByFilter(PackageFilter.PackageFilterCond filterCond, Pageable pageable);

    Page<PackageFilter.PackageFilterForAdminResponseDto> searchPackageForAdmin(PackageFilter.PackageFilterCondForAdmin filterCond, Pageable pageable);
}
