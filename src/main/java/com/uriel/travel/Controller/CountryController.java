package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.Country;
import com.uriel.travel.dto.product.PackageResponseDto;
import com.uriel.travel.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryController {

    private final PackageService packageService;

    // 전체 여행지 조회
    @GetMapping
    public BaseResponse<List<Country>> getAllCountries() {
        return BaseResponse.ok(Stream.of(Country.values()).collect(Collectors.toList()));
    }

    // 여행지별 패키지 목록 조회
    @GetMapping("/{countryName}")
    public BaseResponse<List<PackageResponseDto.PackageInfo>> getPackageByCountryName(@PathVariable String countryName) {
        return BaseResponse.ok(packageService.getPackagesByCountry(countryName));
    }
}
