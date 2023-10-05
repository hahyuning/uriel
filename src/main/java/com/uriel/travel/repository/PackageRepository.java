package com.uriel.travel.repository;

import com.uriel.travel.domain.Package;
import com.uriel.travel.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package,Long> {
}
