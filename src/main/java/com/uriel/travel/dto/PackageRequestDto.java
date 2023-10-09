package com.uriel.travel.dto;

import com.uriel.travel.domain.Package;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

public class PackageRequestDto {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {

        String packageName;
        String summary;
        int period;
        String country;
        String theme;
        String familyMember;
        int price;
        String season;
        String tag;
        String hotelInfo;
        String regionInfo;
        String terms;

        public Package toEntity() {
            return Package.builder()
                    .packageName(packageName)
                    .summary(summary)
                    .period(period)
                    .country(country)
                    .theme(theme)
                    .familyMember(familyMember)
                    .price(price)
                    .season(season)
                    .hotelInfo(hotelInfo)
                    .regionInfo(regionInfo)
                    .terms(terms).build();
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {

        Long packageId;
        String packageName;
        String summary;
        int period;
        String country;
        String theme;
        String familyMember;
        int price;
        String season;
        String hotelInfo;
        String regionInfo;
        String terms;

        public Package toEntity() {
            return Package.builder()
                    .id(packageId)
                    .packageName(packageName)
                    .summary(summary)
                    .period(period)
                    .country(country)
                    .theme(theme)
                    .familyMember(familyMember)
                    .price(price)
                    .season(season)
                    .hotelInfo(hotelInfo)
                    .regionInfo(regionInfo)
                    .terms(terms).build();
        }
    }
}
