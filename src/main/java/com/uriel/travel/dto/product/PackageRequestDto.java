package com.uriel.travel.dto.product;

import com.uriel.travel.domain.Country;
import com.uriel.travel.domain.Package;
import com.uriel.travel.domain.Schedule;
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
        String hotelInfoMd;
        String hotelInfoHtml;
        String regionInfoMd;
        String regionInfoHtml;
        String termsMd;
        String termsHtml;
        List<ScheduleRequestDto> scheduleList;

        public Package toEntity() {
            return Package.builder()
                    .packageName(packageName)
                    .country(Country.from(countryName))
                    .summary(summary)
                    .period(period)
                    .hashTag(hashTag)
                    .hotelInfoMd(hotelInfoMd)
                    .hotelInfoHtml(hotelInfoMd)
                    .regionInfoMd(regionInfoMd)
                    .regionInfoHtml(regionInfoHtml)
                    .termsMd(termsMd)
                    .termsHtml(termsHtml).build();
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
        String hotelInfoMd;
        String hotelInfoHtml;
        String regionInfoMd;
        String regionInfoHtml;
        String termsMd;
        String termsHtml;
        List<ScheduleRequestDto> scheduleList;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ScheduleRequestDto {

        Long scheduleId;
        int day;
        String dayContentMd;
        String dayContentHtml;
        String hotel;
        String meal;
        String vehicle;

        public Schedule toEntity() {
            return Schedule
                    .builder()
                    .day(day)
                    .dayContentMd(dayContentMd)
                    .dayContentHtml(dayContentHtml)
                    .hotel(hotel)
                    .meal(meal)
                    .vehicle(vehicle)
                    .build();
        }
    }
}
