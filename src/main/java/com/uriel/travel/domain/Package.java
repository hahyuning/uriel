package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uriel.travel.domain.enumeration.Country;
import com.uriel.travel.domain.enumeration.TagType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Package extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    Long id;

    String packageName;

    String summary;

    @Enumerated(EnumType.STRING)
    Country country;

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

    @Enumerated(EnumType.STRING)
    TagType tagType;

    String tagContent;

    @JsonIgnore
    @OneToMany(mappedBy = "aPackage")
    List<Schedule> scheduleList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "aPackage")
    List<Product> productList = new ArrayList<>();
}
