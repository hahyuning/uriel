package com.uriel.travel.dto.product;

import com.uriel.travel.domain.Package;
import com.uriel.travel.dto.editor.ImageDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PackageResponseDto {

    List<PackageResponseDto.PackageInfo> packageList;
    TagResponseDto.GetAllTags allTags;
    List<CountryResponseDto.CountryInfo> allCountries;

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PackageInfo {

        Long packageId;
        String packageName;
        String summary;
        int period;
        String privacy;
        String countryName;
        int price;
        String hashTag;
        String hotelInfo;
        String regionInfo;
        String terms;
        List<ImageDto> thumbnailList;
        List<ScheduleDto> scheduleList;
        List<TagResponseDto.TagInfo> checkedTagList = new ArrayList<>(); // 체크된 태그 리스트

        public static PackageResponseDto.PackageInfo of(Package aPackage) {
            return PackageInfo.builder()
                    .packageId(aPackage.getId())
                    .packageName(aPackage.getPackageName())
                    .summary(aPackage.getSummary())
                    .period(aPackage.getPeriod())
                    .privacy(aPackage.getIsPublic().getViewName())
                    .countryName(aPackage.getCountry().getCountryName())
                    .hashTag(aPackage.getHashTag())
                    .hotelInfo(aPackage.getHotelInfo())
                    .regionInfo(aPackage.getRegionInfo())
                    .terms(aPackage.getTerms())
                    .build();
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }

//    @Getter
//    @Setter
//    @Builder
//    @FieldDefaults(level = AccessLevel.PRIVATE)
//    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//    public static class GetPackageByCountry {
//        List<PackageResponseDto.PackageInfo> packageList;
//        TagResponseDto.GetAllTags allTags;
//        List<CountryResponseDto.CountryInfo> allCountries;
//
//    }
}
