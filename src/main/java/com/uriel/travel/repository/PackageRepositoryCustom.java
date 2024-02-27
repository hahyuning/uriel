package com.uriel.travel.repository;

import com.uriel.travel.domain.dto.filterCond.PackageFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PackageRepositoryCustom {

    List<PackageFilter.PackageFilterResponseDto> searchPackageByFilter(PackageFilter.PackageFilterCond filterCond);

    Page<PackageFilter.PackageFilterForAdminResponseDto> searchPackageForAdmin(PackageFilter.PackageFilterCondForAdmin filterCond, Pageable pageable);
}
