package com.uriel.travel.repository;

import com.uriel.travel.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    // 여행지명으로 조회
    Country findByCountryName(String countryName);
}
