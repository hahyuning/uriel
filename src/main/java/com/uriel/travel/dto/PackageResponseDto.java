package com.uriel.travel.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.uriel.travel.domain.Package;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

public class PackageResponseDto {

    @Getter
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
        String hotelInfo;
        String regionInfo;
        String terms;

        public static PackageResponseDto.GetPackage of(Package aPackage) {
            return GetPackage.builder()
                    .packageId(aPackage.getId())
                    .packageName(aPackage.getPackageName())
                    .summary(aPackage.getSummary())
                    .period(aPackage.getPeriod())
                    .country(aPackage.getCountry())
                    .price(aPackage.getPrice())
                    .hotelInfo(aPackage.getHotelInfo())
                    .regionInfo(aPackage.getRegionInfo())
                    .terms(aPackage.getTerms())
                    .build();
        }
    }
}
