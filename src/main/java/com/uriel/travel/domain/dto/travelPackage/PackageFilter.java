package com.uriel.travel.domain.dto.travelPackage;

import com.querydsl.core.annotations.QueryProjection;
import com.uriel.travel.domain.Country;
import com.uriel.travel.domain.Release;
import com.uriel.travel.domain.SaveState;
import com.uriel.travel.domain.dto.ImageDto;
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
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PackageFilterCondForAdmin {

        String countryName;
        String privacy;
        String saveState;

        Integer periodOrder;

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
        Long price;
        String country;
        String hashTag;
        List<ImageDto> thumbnailList;

        @QueryProjection
        public PackageFilterResponseDto(Long packageId, String packageName, String summary, int period, Country country, String hashTag) {
            this.packageId = packageId;
            this.packageName = packageName;
            this.summary = summary;
            this.period = period;
            this.country = country.getViewName();
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
        String saveState;
        String privacy;

        @QueryProjection
        public PackageFilterForAdminResponseDto(Long packageId, String packageName, Country country, int period, SaveState saveState, Release privacy) {
            this.packageId = packageId;
            this.packageName = packageName;
            this.period = period;
            this.countryName = country.getViewName();
            this.saveState = saveState.getViewName();
            this.privacy = privacy.getViewName();
        }
    }
}
