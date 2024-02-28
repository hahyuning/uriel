package com.uriel.travel.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    String paymentKey; // 결제 키 (toss 요청용)

    String orderNumber;  // toss 응답 -> orderId

    LocalDateTime orderedDate; // toss 응답 -> approveAt

    String method; // 결제 방법

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

    @Builder.Default
    int adultCount = 0;

    @Builder.Default
    int childCount = 0;

    @Builder.Default
    int infantCount = 0;

    @Builder.Default
    int totalPrice = 0; // 상품 수정 시 변경되어야 함

    @Builder.Default
    int payedPrice = 0; // 결제 완료한 금액

    OrderState orderState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User reserveUser;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Traveler> travelerList = new ArrayList<>();

    public void setProduct(Product product) {
        this.product = product;
    }

    // 예약금 10% 결제
    public void partialPayment(int totalPrice) {
        this.totalPrice = totalPrice;
        this.orderState = OrderState.PARTIAL_PAYMENT;
    }

    public void setReserveUser(User user) {
        this.reserveUser = user;
        user.getOrderList().add(this);
    }

    // 예약금 완납
    public void fullPayment() {
        this.orderState = OrderState.FULL_PAYMENT;
    }

    // 전체 환불
    public void fullRefund() {
    }

    // 부분 환불
    public void partialRefund() {
    }

    public void updateOrderState(String orderState) {
        this.orderState = OrderState.from(orderState);
    }
}
