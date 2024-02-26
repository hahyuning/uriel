package com.uriel.travel.domain.dto.order;

import com.uriel.travel.domain.entity.Traveler;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TravelerInfo {

    String travelerName;
    String enFirstName;
    String enLastName;

    String gender;
    String birthDate;
    String phoneNumber;

    boolean isRepresentative;

    public TravelerInfo(Traveler traveler) {
        this.travelerName = traveler.getTravelerName();
        this.enFirstName = traveler.getEnFirstName();
        this.enLastName = traveler.getEnLastName();
        this.gender = traveler.getGender().toString();
        this.birthDate = traveler.getGender().getViewName();
        this.phoneNumber = traveler.getPhoneNumber();
        this.isRepresentative = traveler.isRepresentative();
    }
}
