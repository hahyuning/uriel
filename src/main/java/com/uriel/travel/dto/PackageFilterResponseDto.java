package com.uriel.travel.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PackageFilterResponseDto {

    Long packageId;
    String packageName;
    String summary;
    int period;
    String country;
    int price;
    String hashTag;
    List<ImageDto> thumbnailList;

    @QueryProjection
    public PackageFilterResponseDto(Long packageId, String packageName, String summary, int period, String country, int price, String hashTag) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.summary = summary;
        this.period = period;
        this.country = country;
        this.price = price;
        this.hashTag = hashTag;
    }
}
