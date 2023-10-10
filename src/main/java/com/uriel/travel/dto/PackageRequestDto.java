package com.uriel.travel.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
        List<ScheduleDto> scheduleList;

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
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Update {

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
        List<ScheduleDto> scheduleList;
    }
}
