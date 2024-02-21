package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.dto.product.CountryRequestDto;
import com.uriel.travel.dto.product.CountryResponseDto;
import com.uriel.travel.dto.product.PackageResponseDto;
import com.uriel.travel.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    // 여행지 등록
    @PostMapping("/create")
    public BaseResponse<Void> create(@RequestBody CountryRequestDto.Create requestDto) {
        countryService.create(requestDto);
        return BaseResponse.ok();
    }

    // 여행지 삭제
    @DeleteMapping("/{countryId}")
    public BaseResponse<Void> delete(@PathVariable Long countryId) {
        countryService.delete(countryId);
        return BaseResponse.ok();
    }

    // 전체 여행지 조회
    @GetMapping
    public BaseResponse<List<CountryResponseDto.CountryInfo>> getAllCountries() {
        return BaseResponse.ok(countryService.getAllCountries());
    }

    // 여행지별 패키지 목록 조회
    @GetMapping("/{countryName}")
    public BaseResponse<PackageResponseDto> getPackageByCountryName(@PathVariable String countryName) {
        return BaseResponse.ok(countryService.getPackageByCountry(countryName));
    }
}
