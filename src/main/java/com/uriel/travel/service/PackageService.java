package com.uriel.travel.service;

import com.uriel.travel.domain.Package;
import com.uriel.travel.domain.*;
import com.uriel.travel.dto.editor.ImageDto;
import com.uriel.travel.dto.filterCond.PackageFilter;
import com.uriel.travel.dto.product.*;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PackageService {

    private final PackageRepository packageRepository;
    private final PackageRepositoryCustomImpl packageRepositoryCustom;
    private final ThumbnailRepository thumbnailRepository;
    private final CountryRepository countryRepository;
    private final TagRepository tagRepository;

    // 패키지 임시 저장
    public Long temporarySave(PackageRequestDto.Create requestDto) {
        Country country = countryRepository.findByCountryName(requestDto.getCountryName());
        Package aPackage = requestDto.toEntity();
        aPackage.setCountry(country);
        return packageRepository.save(aPackage).getId();
    }

    // 패키지 저장
    public Long create(PackageRequestDto.Create requestDto) {
        Country country = countryRepository.findByCountryName(requestDto.getCountryName());
        Package aPackage = requestDto.toEntity();
        aPackage.setPrivacy(requestDto.getPrivacy());
        aPackage.setCountry(country);
        return packageRepository.save(aPackage).getId();
    }

    // 패키지 수정
    public void update(PackageRequestDto.Update requestDto, Long id) {
        Package packageById = packageRepository.findById(id)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        Country country = countryRepository.findByCountryName(requestDto.getCountryName());
        packageById.setPrivacy(requestDto.getPrivacy());
        packageById.update(requestDto, country);
    }

    // 패키지 임시저장 업데이트
    public void temporaryUpdate(PackageRequestDto.Update requestDto, Long id) {
        Package packageById = packageRepository.findById(id)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        Country country = countryRepository.findByCountryName(requestDto.getCountryName());
        packageById.update(requestDto, country);
        packageById.setPrivacy(Release.TEMPORARY.getViewName());
    }

    // 패키지 삭제
    public void delete(List<Long> ids) {
        ids.forEach(id -> {
            Package aPackage = packageRepository.findById(id)
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            packageRepository.delete(aPackage);
        });
    }

    // 패키지 공개/비공개 처리
    public void privacyUpdate(String privacy, List<Long> ids) {
        ids.forEach(id -> {
            Package aPackage = packageRepository.findById(id)
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            aPackage.setPrivacy(privacy);
        });
    }

    // 패키지 상세 조회
    @Transactional(readOnly = true)
    public PackageResponseDto getPackageById(Long id) {

        List<PackageResponseDto.PackageInfo> packageInfos = new ArrayList<>();

        // 패키지 기본 정보
        Package packageById = packageRepository.findById(id)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        PackageResponseDto.PackageInfo packageInfo = PackageResponseDto.PackageInfo.of(packageById);

        // 썸네일
        List<ImageDto> thumbnailList = new ArrayList<>();
        for (Thumbnail thumbnail : packageById.getThumbnailList()) {
            thumbnailList.add(ImageDto.builder()
                    .originalImageName(thumbnail.getOriginalImageName())
                    .uploadImageName(thumbnail.getUploadImageName())
                    .imagePath(thumbnail.getImagePath())
                    .imageUrl(thumbnail.getImageUrl()).build());
        }
        packageInfo.setThumbnailList(thumbnailList);

        // 일정
        List<ScheduleDto> scheduleList = new ArrayList<>();
        for (Schedule schedule : packageById.getScheduleList()) {
            scheduleList.add(ScheduleDto.builder()
                    .scheduleId(schedule.getId())
                    .day(schedule.getDay())
                    .dayContent(schedule.getDayContent())
                    .hotel(schedule.getHotel())
                    .meal(schedule.getMeal())
                    .vehicle(schedule.getVehicle()).build());
        }

        packageInfo.setScheduleList(scheduleList);

        // 최저가 계산
        int minPrice = 0;
        for (Product product : packageById.getProductList()) {

            if (product.getStartDate().isAfter(LocalDateTime.now()) && product.getPrice() != 0) {

                if (minPrice == 0 || product.getPrice() <= minPrice) {
                    minPrice = product.getPrice();
                }

            }
        }
        packageInfo.setPrice(minPrice);

        // 체크된 태그 리스트
        List<TagResponseDto.TagInfo> checkedTagList = new ArrayList<>();
        for (Tagging tagging : packageById.getTaggingList()) {
            checkedTagList.add(TagResponseDto.TagInfo.builder()
                    .tagId(tagging.getTag().getId())
                    .tagContent(tagging.getTag().getTagContent()).build());
        }
        packageInfo.setCheckedTagList(checkedTagList);

        packageInfos.add(packageInfo);

        // 전체 나라 정보
        List<CountryResponseDto.CountryInfo> allCountries = new ArrayList<>();
        for (Country c : countryRepository.findAll()) {
            allCountries.add(CountryResponseDto.CountryInfo.of(c));
        }

        TagResponseDto.GetAllTags allTags = TagResponseDto.GetAllTags.of(
                tagRepository.findByTagType(TagType.THEME.toString()),
                tagRepository.findByTagType(TagType.FAMILY.toString()),
                tagRepository.findByTagType(TagType.SEASON.toString()),
                tagRepository.findByTagType(TagType.PRICE.toString()));

        return PackageResponseDto
                .builder()
                .packageList(packageInfos)
                .allTags(allTags)
                .allCountries(allCountries).build();
    }

    // 패키지 태그 검색
    @Transactional(readOnly = true)
    public Page<PackageFilter.PackageFilterResponseDto> packageSearchByFilterCond(PackageFilter.PackageFilterCond filterCond) {
        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), filterCond.getLimit());
        Page<PackageFilter.PackageFilterResponseDto> responseDtos = packageRepositoryCustom.searchPackageByFilter(filterCond, pageRequest);

        responseDtos.forEach(dto -> {

            List<Thumbnail> thumbnails = thumbnailRepository.findAllByPackageId(dto.getPackageId());
            List<ImageDto> thumbnailList = new ArrayList<>();
            thumbnails.forEach(thumbnail -> {
                thumbnailList.add(ImageDto.builder()
                        .originalImageName(thumbnail.getOriginalImageName())
                        .uploadImageName(thumbnail.getUploadImageName())
                        .imagePath(thumbnail.getImagePath())
                        .imageUrl(thumbnail.getImageUrl()).build());

            });

            dto.setThumbnailList(thumbnailList);

            // 최저가 계산
            Package aPackage = packageRepository.findById(dto.getPackageId())
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            int minPrice = 0;
            for (Product product : aPackage.getProductList()) {

                if (product.getStartDate().isAfter(LocalDateTime.now()) && product.getPrice() != 0) {

                    if (minPrice == 0 || product.getPrice() <= minPrice) {
                        minPrice = product.getPrice();
                    }

                }
            }
            dto.setPrice(minPrice);
        });
        return responseDtos;
    }

    public Page<PackageFilter.PackageFilterForAdminResponseDto> packageByCountryForAdmin(PackageFilter.PackageFilterCondForAdmin filterCond) {
        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), filterCond.getLimit());
        return packageRepositoryCustom.searchByCountryForAdmin(filterCond, pageRequest);
    }

    public PackageResponseDto getAllPackages() {
        // 패키지 기본 정보
        List<PackageResponseDto.PackageInfo> packageDtoList = new ArrayList<>();
        List<Package> packageList = packageRepository.findAllByIsPublic();
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
