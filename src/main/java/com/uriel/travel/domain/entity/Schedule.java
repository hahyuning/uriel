package com.uriel.travel.domain.entity;

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

    @Column(columnDefinition = "LONGTEXT")
    String dayContentMd;
    @Column(columnDefinition = "LONGTEXT")
    String dayContentHtml;

    @Column(columnDefinition = "TEXT")
    String hotel;

    @Column(columnDefinition = "TEXT")
    String meal;

    @Column(columnDefinition = "TEXT")
    String vehicle;

    public void setPackage(Package aPackage) {
        this.aPackage = aPackage;
        aPackage.getScheduleList().add(this);
    }

    public void idInit() {
        this.id = null;
    }
}
