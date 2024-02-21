package com.uriel.travel.service;

import com.uriel.travel.domain.*;
import com.uriel.travel.domain.Package;
import com.uriel.travel.dto.editor.ImageDto;
import com.uriel.travel.dto.product.CountryRequestDto;
import com.uriel.travel.dto.product.CountryResponseDto;
import com.uriel.travel.dto.product.PackageResponseDto;
import com.uriel.travel.dto.product.TagResponseDto;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.CountryRepository;
import com.uriel.travel.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CountryService {

    private final CountryRepository countryRepository;
    private final TagRepository tagRepository;

    // 여행지 저장
    public void create(CountryRequestDto.Create requestDto) {
        countryRepository.save(requestDto.toEntity());
    }

    // 여행지 삭제
    public void delete(Long countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        countryRepository.delete(country);
    }

    // 전체 여행지 조회
    @Transactional(readOnly = true)
    public List<CountryResponseDto.CountryInfo> getAllCountries() {
        List<Country> allCountries = countryRepository.findAll();
        List<CountryResponseDto.CountryInfo> result = new ArrayList<>();

        allCountries.forEach(country -> {
            result.add(CountryResponseDto.CountryInfo.of(country));
        });
        return result;
    }

    // 여행지별 패키지 검색
    @Transactional(readOnly = true)
    public PackageResponseDto getPackageByCountry(String countryName) {

        Country country = countryRepository.findByCountryName(countryName);

        // 패키지 기본 정보
        List<PackageResponseDto.PackageInfo> packageDtoList = new ArrayList<>();
        List<Package> packageList = country.getPackageList();
        for (Package aPackage : packageList) {

            PackageResponseDto.PackageInfo packageInfo = PackageResponseDto.PackageInfo.of(aPackage);
            // 최저가 계산
            int minPrice = 0;
            for (Product product : aPackage.getProductList()) {
                if (product.getStartDate().isAfter(LocalDateTime.now()) && product.getPrice() != 0 && product.getPrice() <= minPrice) {
                    minPrice = product.getPrice();
                }
            }
            packageInfo.setPrice(minPrice);

            // 썸네일
            List<ImageDto> imageDtos = new ArrayList<>();
            for (Thumbnail thumbnail : aPackage.getThumbnailList()) {
                imageDtos.add(ImageDto.builder()
                        .imageUrl(thumbnail.getImageUrl())
                        .uploadImageName(thumbnail.getUploadImageName())
                        .imagePath(thumbnail.getImagePath())
                        .originalImageName(thumbnail.getOriginalImageName())
                        .build());
            }
            packageInfo.setThumbnailList(imageDtos);
            packageDtoList.add(packageInfo);
        }

        // 전체 나라 정보
        List<Country> all = countryRepository.findAll();
        List<CountryResponseDto.CountryInfo> allCountries = new ArrayList<>();
        for (Country c : all) {
            allCountries.add(CountryResponseDto.CountryInfo.of(c));
        }

        return PackageResponseDto.builder()
                .packageList(packageDtoList)
                .allTags(TagResponseDto.GetAllTags.of( // 전체 태그 정보
                        tagRepository.findByTagType(TagType.THEME.toString()),
                        tagRepository.findByTagType(TagType.FAMILY.toString()),
                        tagRepository.findByTagType(TagType.SEASON.toString()),
                        tagRepository.findByTagType(TagType.PRICE.toString())))
                .allCountries(allCountries).build();
    }
}
