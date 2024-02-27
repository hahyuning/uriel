package com.uriel.travel.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uriel.travel.domain.Country;
import com.uriel.travel.domain.OrderState;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "ORDERS")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    Long id;

    @Column(name = "toss_order_id")
    String orderId;

    LocalDateTime orderedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pacakge_id")
    Package aPackage;

    @Enumerated
    Country country;

    @Builder.Default
    int adultCount = 0;

    @Builder.Default
    int childCount = 0;

    @Builder.Default
    int infantCount = 0;

    @Enumerated
    OrderState orderState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User reserveUser;

    @Builder.Default
    int deposit = 0;

    @Builder.Default
    int balance = 0;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Traveler> travelerList = new ArrayList<>();
}
