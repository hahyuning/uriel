package com.uriel.travel.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    Package aPackage;

    int day;

    @Lob
    String dayContentMd;
    @Lob
    String dayContentHtml;

    @Lob
    String hotel;

    @Lob
    String meal;

    @Lob
    String vehicle;

    public void setPackage(Package aPackage) {
        this.aPackage = aPackage;
        aPackage.getScheduleList().add(this);
    }
}
