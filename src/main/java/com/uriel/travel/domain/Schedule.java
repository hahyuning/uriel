package com.uriel.travel.domain;

import com.uriel.travel.dto.ScheduleDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Schedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    Package aPackage;

    int day;

    @Lob
    String dayContent;

    @Lob
    String hotel;

    @Lob
    String meal;

    @Lob
    String vehicle;

    public Schedule(ScheduleDto dto) {
        this.day = dto.getDay();
        this.dayContent = dto.getDayContent();
        this.hotel = dto.getHotel();
        this.meal = dto.getMeal();
        this.vehicle = dto.getVehicle();
    }

    public void setPackage(Package aPackage) {
        this.aPackage = aPackage;
        aPackage.getScheduleList().add(this);
    }
}
