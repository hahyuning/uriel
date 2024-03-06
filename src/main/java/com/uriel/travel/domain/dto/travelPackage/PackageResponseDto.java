package com.uriel.travel.domain.dto.travelPackage;

import com.uriel.travel.domain.dto.ImageDto;
import com.uriel.travel.domain.dto.tag.TagResponseDto;
import com.uriel.travel.domain.entity.Package;
import com.uriel.travel.domain.entity.Schedule;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PackageResponseDto {

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PackageInfo {

        Long packageId;
        String packageName;
        String summary;
        int period;
        String privacy;
        String countryName;
        int price;
        String hashTag;
        String hotelInfo;
        String regionInfo;
        String terms;
        List<ImageDto> thumbnailList;
        List<ScheduleResponseDto> scheduleList;
        List<TagResponseDto.TagInfo> checkedTagList; // 체크된 태그 리스트

        public static PackageResponseDto.PackageInfo of(Package aPackage, String operation) {

            if (operation.equals("detail")) {
                return PackageInfo.builder()
                        .packageId(aPackage.getId())
                        .packageName(aPackage.getPackageName())
                        .countryName(aPackage.getCountry().getViewName())
                        .summary(aPackage.getSummary())
                        .period(aPackage.getPeriod())
                        .privacy(aPackage.getIsPublic().getViewName())
                        .hashTag(aPackage.getHashTag())
                        .hotelInfo(aPackage.getHotelInfoHtml())
                        .regionInfo(aPackage.getRegionInfoHtml())
                        .terms(aPackage.getTermsHtml())
                        .build();
            } else {
                return PackageInfo.builder()
                        .packageId(aPackage.getId())
                        .packageName(aPackage.getPackageName())
                        .countryName(aPackage.getCountry().getViewName())
                        .summary(aPackage.getSummary())
                        .period(aPackage.getPeriod())
                        .privacy(aPackage.getIsPublic().getViewName())
                        .hashTag(aPackage.getHashTag())
                        .hotelInfo(aPackage.getHotelInfoMd())
                        .regionInfo(aPackage.getRegionInfoMd())
                        .terms(aPackage.getTermsMd())
                        .build();
            }
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ScheduleResponseDto {

        Long scheduleId;
        int day;
        String dayContent;
        String hotel;
        String meal;
        String vehicle;

        public static PackageResponseDto.ScheduleResponseDto of(Schedule schedule, String operation) {

            if (operation.equals("detail")) {
                return ScheduleResponseDto.builder()
                        .scheduleId(schedule.getId())
                        .day(schedule.getDay())
                        .dayContent(schedule.getDayContentHtml())
                        .hotel(schedule.getHotel())
                        .meal(schedule.getMeal())
                        .vehicle(schedule.getVehicle())
                        .build();
            } else {
                return ScheduleResponseDto.builder()
                        .scheduleId(schedule.getId())
                        .day(schedule.getDay())
                        .dayContent(schedule.getDayContentMd())
                        .hotel(schedule.getHotel())
                        .meal(schedule.getMeal())
                        .vehicle(schedule.getVehicle())
                        .build();
            }
        }
    }

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PackageInfoSimple {

        Long packageId;
        String packageName;

        public PackageInfoSimple(Package aPackage) {
            this.packageId = aPackage.getId();
            this.packageName = aPackage.getPackageName();
        }
    }
}
