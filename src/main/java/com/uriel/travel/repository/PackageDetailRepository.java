package com.uriel.travel.repository;

import com.uriel.travel.domain.PackageDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageDetailRepository extends JpaRepository<PackageDetail, Long> {
}
