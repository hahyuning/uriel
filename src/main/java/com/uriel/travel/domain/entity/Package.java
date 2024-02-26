package com.uriel.travel.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uriel.travel.domain.Country;
import com.uriel.travel.domain.Release;
import com.uriel.travel.domain.SaveState;
import com.uriel.travel.domain.dto.travelPackage.PackageRequestDto;
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

    Release isPublic;

    SaveState saveState;

    String packageName;

    String summary;

    int period;

    Country country;

    String hashTag;

    @Lob
    String hotelInfoMd;
    String hotelInfoHtml;

    @Lob
    String regionInfoMd;
    String regionInfoHtml;

    @Lob
    String termsMd;
    String termsHtml;

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
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Product> productList = new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Tagging> taggingList = new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Order> orderList = new ArrayList<>();

    public void update(PackageRequestDto.Update requestDto) {
        this.packageName = requestDto.getPackageName();
        this.summary = requestDto.getSummary();
        this.period = requestDto.getPeriod();
        this.hashTag = requestDto.getHashTag();
        this.hotelInfoMd = requestDto.getHotelInfoMd();
        this.hotelInfoHtml = requestDto.getHotelInfoMd();
        this.regionInfoMd = requestDto.getRegionInfoMd();
        this.regionInfoHtml = requestDto.getRegionInfoHtml();
        this.termsMd = requestDto.getTermsMd();
        this.termsHtml = requestDto.getTermsHtml();
        this.country = Country.from(requestDto.getCountryName());
    }

    public void setPrivacy(String privacy) {
        this.isPublic = Release.from(privacy);
    }
    public void setSaveState(SaveState saveState) {
        this.saveState = saveState;
    }

    public Package duplicate(Package aPackage) {
        return Package.builder()
                .isPublic(aPackage.getIsPublic())
                .saveState(aPackage.getSaveState())
                .packageName(aPackage.getPackageName())
                .summary(aPackage.getSummary())
                .period(aPackage.getPeriod())
                .country(aPackage.getCountry())
                .hashTag(aPackage.getHashTag())
                .hotelInfoMd(aPackage.getHotelInfoMd())
                .hotelInfoHtml(aPackage.getHotelInfoHtml())
                .regionInfoMd(aPackage.getRegionInfoMd())
                .regionInfoHtml(aPackage.getRegionInfoHtml())
                .termsMd(aPackage.getTermsMd())
                .termsHtml(aPackage.getTermsHtml())
                .saveState(SaveState.TEMPORARY)
                .build();
    }
}
