package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uriel.travel.dto.product.PackageRequestDto;
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

    @Enumerated(EnumType.STRING)
    @Builder.Default
    Release isPublic = Release.TEMPORARY;

    String packageName;

    String summary;

    int period;

    @Enumerated(EnumType.STRING)
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
        this.isPublic = Release.from(requestDto.getPrivacy());
    }

    public void setPrivacy(String privacy) {
        this.isPublic = Release.from(privacy);
    }
}
