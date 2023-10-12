package com.uriel.travel.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.uriel.travel.domain.Package;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

public class PackageResponseDto {

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetPackage {

        Long packageId;
        String packageName;
        String summary;
        int period;
        String country;
        int price;
        String hashTag;
        String hotelInfo;
        String regionInfo;
        String terms;
        List<ImageDto> thumbnailList;
        List<ScheduleDto> scheduleList;

        public static PackageResponseDto.GetPackage of(Package aPackage) {
            return GetPackage.builder()
                    .packageId(aPackage.getId())
                    .packageName(aPackage.getPackageName())
                    .summary(aPackage.getSummary())
                    .period(aPackage.getPeriod())
                    .country(aPackage.getCountry())
                    .hashTag(aPackage.getHashTag())
                    .price(aPackage.getPrice())
                    .hotelInfo(aPackage.getHotelInfo())
                    .regionInfo(aPackage.getRegionInfo())
                    .terms(aPackage.getTerms())
                    .build();
        }
    }

    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetAllTags {

        List<String> themeList;
        List<String> familyList;
        List<String> seasonList;

        public static PackageResponseDto.GetAllTags of(List<String> themeList,
                                                       List<String> familyList,
                                                       List<String> seasonList) {
            return GetAllTags.builder()
                    .themeList(themeList)
                    .familyList(familyList)
                    .seasonList(seasonList)
                    .build();
        }
    }
}
