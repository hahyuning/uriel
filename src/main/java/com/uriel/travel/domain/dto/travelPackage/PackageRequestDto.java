package com.uriel.travel.domain.dto.travelPackage;

import com.uriel.travel.domain.Country;
import com.uriel.travel.domain.entity.Package;
import com.uriel.travel.domain.entity.Schedule;
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
        DayContent dayContent;
        String hotel;
        String meal;
        String vehicle;

        public Schedule toEntity() {
            return Schedule
                    .builder()
                    .day(day)
                    .dayContentMd(dayContent.getDayContentMd())
                    .dayContentHtml(dayContent.getDayContentHtml())
                    .hotel(hotel)
                    .meal(meal)
                    .vehicle(vehicle)
                    .build();
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DayContent {
        String dayContentMd;
        String dayContentHtml;
    }
}