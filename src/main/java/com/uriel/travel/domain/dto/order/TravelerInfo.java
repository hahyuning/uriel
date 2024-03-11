package com.uriel.travel.domain.dto.order;

import com.uriel.travel.domain.Gender;
import com.uriel.travel.domain.entity.Traveler;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TravelerInfo {

    String travelerName;
    String enFirstName;
    String enLastName;

    String gender;
    LocalDate birth;
    String phoneNumber;

    boolean isRepresentative;

    public TravelerInfo(Traveler traveler) {
        this.travelerName = traveler.getTravelerName();
        this.enFirstName = traveler.getEnFirstName();
        this.enLastName = traveler.getEnLastName();
        this.gender = traveler.getGender().getViewName();
        this.birth = traveler.getBirth();
        this.phoneNumber = traveler.getPhoneNumber();
        this.isRepresentative = traveler.isRepresentative();
    }

    public Traveler toEntity() {
        return Traveler.builder()
                .travelerName(this.travelerName)
                .enFirstName(this.enFirstName)
                .enLastName(this.enLastName)
                .gender(Gender.from(this.gender))
                .birth(this.birth)
                .phoneNumber(this.phoneNumber)
                .isRepresentative(this.isRepresentative)
                .build();
    }
}
