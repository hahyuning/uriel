package com.uriel.travel.domain.entity;

import com.uriel.travel.domain.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Traveler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "traveler_id")
    Long id;

    String travelerName;
    String enFirstName;
    String enLastName;

    @Enumerated
    Gender gender;

    String birthDate;

    String phoneNumber;

    @Builder.Default
    boolean isRepresentative = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    Order order;
}
