package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.Airline;
import com.uriel.travel.domain.Country;
import com.uriel.travel.domain.dto.BatchRequestDto;
import com.uriel.travel.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class CountryController {

    private final PackageService packageService;

    // 전체 여행지 조회
    @GetMapping("/countries")
    public BaseResponse<List<String>> getAllCountries() {
        return BaseResponse.ok(Stream.of(Country.values())
                .map(Country::getViewName)
                .collect(Collectors.toList()));
    }

    // 전체 항공사 조회
    @GetMapping("/airlines")
    public BaseResponse<List<String>> getAllAirlines() {
        return BaseResponse.ok(Stream.of(Airline.values())
                .map(Airline::getViewName)
                .collect(Collectors.toList()));
    }

}
