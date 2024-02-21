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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    Country country;

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
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Product> productList = new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Tagging> taggingList = new ArrayList<>();

    public void update(PackageRequestDto.Update requestDto, Country country) {
        this.packageName = requestDto.getPackageName();
        this.summary = requestDto.getSummary();
        this.period = requestDto.getPeriod();
        this.country = country;
        this.hashTag = requestDto.getHashTag();
        this.hotelInfo = requestDto.getHotelInfo();
        this.regionInfo = requestDto.getRegionInfo();
        this.terms = requestDto.getTerms();

        this.isPublic = Release.from(requestDto.getPrivacy());
    }

    public void setPrivacy(String privacy) {
        this.isPublic = Release.from(privacy);
    }

    public void setCountry(Country country) {
        this.country = country;
        country.getPackageList().add(this);
    }
}
