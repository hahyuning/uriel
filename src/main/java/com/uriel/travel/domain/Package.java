package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uriel.travel.dto.PackageRequestDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Package extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    Long id;

    @Builder.Default
    int privacy = 0; // 0: 공개, 1: 비공개

    String packageName;

    String summary;

    int period;

    String country;

    int price;

    String hashTag;

    @Lob
    String hotelInfo;

    @Lob
    String regionInfo;

    @Lob
    String terms;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Thumbnail> thumbnailList = new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Schedule> scheduleList = new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL)
    List<Product> productList = new ArrayList<>();

    public void update(PackageRequestDto.Update requestDto) {
        this.privacy = requestDto.getPrivacy();
        this.packageName = requestDto.getPackageName();
        this.summary = requestDto.getSummary();
        this.period = requestDto.getPeriod();
        this.country = requestDto.getCountry();
        this.hashTag = requestDto.getHashTag();
        this.price = requestDto.getPrice();
        this.hotelInfo = requestDto.getHotelInfo();
        this.regionInfo = requestDto.getRegionInfo();
        this.terms = requestDto.getTerms();
    }
}
