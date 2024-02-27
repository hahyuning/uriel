package com.uriel.travel.domain.dto.order;

import com.uriel.travel.domain.entity.Traveler;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TravelerInfo {

    String travelerName;
    String enFirstName;
    String enLastName;

    String gender;
    String birth;
    String phoneNumber;

    boolean isRepresentative;

    public TravelerInfo(Traveler traveler) {
        this.travelerName = traveler.getTravelerName();
        this.enFirstName = traveler.getEnFirstName();
        this.enLastName = traveler.getEnLastName();
        this.gender = traveler.getGender().toString();
        this.birth = traveler.getBirth();
        this.phoneNumber = traveler.getPhoneNumber();
        this.isRepresentative = traveler.isRepresentative();
    }
}
