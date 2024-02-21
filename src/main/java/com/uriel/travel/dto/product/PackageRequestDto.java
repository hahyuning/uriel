package com.uriel.travel.dto.product;

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
        String privacy;
        String countryName;
        List<String> themeList;
        List<String> familyList;
        List<String> seasonList;
        List<String> priceList;
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
                    .hashTag(hashTag)
                    .hotelInfo(hotelInfo)
                    .regionInfo(regionInfo)
                    .terms(terms).build();
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {

        String privacy;
        String packageName;
        String summary;
        int period;
        String countryName;
        List<String> themeList;
        List<String> familyList;
        List<String> seasonList;
        List<String> priceList;
        String hashTag;
        String hotelInfo;
        String regionInfo;
        String terms;
        List<ScheduleDto> scheduleList;
    }
}
