package com.uriel.travel.dto.filterCond;

import com.querydsl.core.annotations.QueryProjection;
import com.uriel.travel.dto.editor.ImageDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

public class PackageFilter {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PackageFilterCond {

        List<Long> themeList;
        List<Long> familyList;
        List<Long> priceList;
        List<Long> seasonList;

        int offset;
        int limit;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PackageFilterCondForAdmin {

        String countryName;
        int offset;
        int limit;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PackageFilterResponseDto {

        Long packageId;
        String packageName;
        String summary;
        int period;
        int price;
        String country;
        String hashTag;
        List<ImageDto> thumbnailList;

        @QueryProjection
        public PackageFilterResponseDto(Long packageId, String packageName, String summary, int period, String country, String hashTag) {
            this.packageId = packageId;
            this.packageName = packageName;
            this.summary = summary;
            this.period = period;
            this.country = country;
            this.hashTag = hashTag;
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PackageFilterForAdminResponseDto {

        Long packageId;
        String packageName;
        String countryName;
        int period;

        @QueryProjection
        public PackageFilterForAdminResponseDto(Long packageId, String packageName, String countryName, int period) {
            this.packageId = packageId;
            this.packageName = packageName;
            this.period = period;
            this.countryName = countryName;
        }
    }
}
