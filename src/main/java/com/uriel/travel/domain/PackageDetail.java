package com.uriel.travel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PackageDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    Package aPackage;

    String detailTitle;

    @Lob
    String detailContent;
}
