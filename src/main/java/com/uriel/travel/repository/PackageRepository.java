package com.uriel.travel.repository;

import com.uriel.travel.domain.Country;
import com.uriel.travel.domain.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package,Long> {

    @Query("select p from Package p where p.country =:country and p.isPublic = 'PUBLIC'")
    List<Package> findByCountry(Country country);

    @Query("select p from Package p where p.isPublic = 'PUBLIC'")
    List<Package> findAllByIsPublic();
}
