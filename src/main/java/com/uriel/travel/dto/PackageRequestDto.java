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
        int price;
        String country;
        List<String> themeList;
        List<String> familyList;
        List<String> seasonList;
        String hashTag;
        String hotelInfo;
        String regionInfo;
        String terms;
        List<ScheduleDto> scheduleList;

        public Package toEntity() {
            return Package.builder()
                    .packageName(packageName)
                    .summary(summary)
                    .period(period)
                    .price(price)
                    .country(country)
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
        int price;
        String country;
        List<String> themeList;
        List<String> familyList;
        List<String> seasonList;
        String hashTag;
        String hotelInfo;
        String regionInfo;
        String terms;
        List<ScheduleDto> scheduleList;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FilterCond {

        List<Long> themeList;
        List<Long> familyList;
        int cost;
        List<Long> seasonList;
    }
}
