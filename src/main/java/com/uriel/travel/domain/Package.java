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

    String packageName;

    String summary;

    int period;

    String country;

    String theme;

    String familyMember;

    int price;

    String season;

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

    public void update(PackageRequestDto.Update requestDto) {
        this.packageName = requestDto.getPackageName();
        this.summary = requestDto.getSummary();
        this.period = requestDto.getPeriod();
        this.country = requestDto.getCountry();
        this.theme = requestDto.getTheme();
        this.familyMember = requestDto.getFamilyMember();
        this.price = requestDto.getPrice();
        this.season = requestDto.getSeason();
        this.hotelInfo = requestDto.getHotelInfo();
        this.regionInfo = requestDto.getRegionInfo();
        this.terms = requestDto.getTerms();
    }
}
