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
    int totalPrice = 0;

    @Builder.Default
    int deposit = 0;

    @Builder.Default
    int balance = 0;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Traveler> travelerList = new ArrayList<>();

    public void setMapping(Product product, Package aPackage) {
        this.product = product;

        this.aPackage = aPackage;
        aPackage.getOrderList().add(this);

        this.country = aPackage.getCountry();
    }

    public void partialPayment(int totalPrice) {
        this.totalPrice = totalPrice;
        this.deposit = (totalPrice / 100) * 10;
        this.balance = totalPrice - (totalPrice / 100) * 10;
        this.orderState = OrderState.COMPLETE;
    }

    public void setReserveUser(User user) {
        this.reserveUser = user;
        user.getOrderList().add(this);
    }

    public void fullPayment() {
        this.balance = 0;
        this.totalPrice = this.deposit + this.balance;

        this.orderState = OrderState.FULL_PAYMENT;
    }

    public void updateBalance(int balance) {
        this.balance = balance;

        if (this.balance == 0) {
            this.orderState = OrderState.FULL_PAYMENT;
        }
    }

    public void updateOrderState(String orderState) {
        this.orderState = OrderState.from(orderState);
    }
}
