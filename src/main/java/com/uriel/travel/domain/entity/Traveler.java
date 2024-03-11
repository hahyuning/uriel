package com.uriel.travel.domain.entity;

import com.uriel.travel.domain.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    LocalDate birth;

    String phoneNumber;

    boolean isRepresentative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    Order order;

    public void setOrder(Order order) {
        this.order = order;
        order.getTravelerList().add(this);
    }
}
