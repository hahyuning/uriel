package com.uriel.travel.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uriel.travel.domain.OrderState;
import com.uriel.travel.domain.dto.order.OrderRequestDto;
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

//    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    Long id;

    @Column(name = "imom_order_id")
    String imomOrderId;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "ORDER_NUMBER", joinColumns = @JoinColumn(name = "order_id"))
    List<String> orderNumberList = new ArrayList<>();  // toss 응답 -> orderId

    LocalDateTime orderDate; // toss 응답 -> approveAt

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
    int totalCount = 0; // 여행자 변경 시 변경 필요

    @Builder.Default
    Long totalPrice = 0L; // 상품 수정 시 변경되어야 함

    @Builder.Default
    Long payedPrice = 0L; // 결제 완료한 금액

    @Builder.Default
    Long additionalPrice = 0L;

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

    public void setReserveUser(User user) {
        this.reserveUser = user;
        user.getOrderList().add(this);
    }

    public void additionalPayment(Long totalAmount) {
        this.payedPrice += totalAmount;

        if (this.totalPrice < this.payedPrice + this.additionalPrice) {
            this.orderState = OrderState.REFUND_NEEDED;
        } else if (this.totalPrice == this.payedPrice + this.additionalPrice) {
            this.orderState = OrderState.COMPLETED;
        } else {
            this.orderState = OrderState.PAYMENT_NEEDED;
        }
    }

    public void addOrderNumber(String orderNumber) {
        this.orderNumberList.add(orderNumber);
    }

    public void updateTotalPriceWithProductPriceChange(Long newTotalPrice) {
        this.totalPrice = newTotalPrice;

        if (this.totalPrice < this.payedPrice + this.additionalPrice) {
            this.orderState = OrderState.REFUND_NEEDED;
        } else if (this.totalPrice == this.payedPrice + this.additionalPrice) {
            this.orderState = OrderState.COMPLETED;
        } else {
            this.orderState = OrderState.PAYMENT_NEEDED;
        }
    }

    public void updateTravelerAndPrice(OrderRequestDto.UpdateTraveler requestDto, ProductDetail productDetail) {
        this.totalPrice = (productDetail.getAdultPrice() + productDetail.getAdultSurcharge()) * requestDto.getAdultCount()
                + (productDetail.getChildPrice() + productDetail.getChildSurcharge()) * requestDto.getChildCount()
                + (productDetail.getInfantPrice() + productDetail.getInfantSurcharge()) * requestDto.getInfantCount();

        this.adultCount = requestDto.getAdultCount();
        this.childCount = requestDto.getChildCount();
        this.infantCount = requestDto.getInfantCount();

        if (this.totalPrice < this.payedPrice + this.additionalPrice) {
            this.orderState = OrderState.REFUND_NEEDED;
        } else if (this.totalPrice == this.payedPrice + this.additionalPrice) {
            this.orderState = OrderState.COMPLETED;
        } else {
            this.orderState = OrderState.PAYMENT_NEEDED;
        }
    }

    public void cancel(Long cancelAmount) {
        this.payedPrice -= cancelAmount;

        if (this.payedPrice.equals(0L)) {
            this.orderState = OrderState.CANCELED;
        } else {
            this.orderState = OrderState.PAYMENT_NEEDED;
        }
    }

    public void updateAdditionalPrice(Long additionalPrice) {
        if (!this.orderState.equals(OrderState.CANCELED)) {

            this.additionalPrice = additionalPrice;
            if (this.totalPrice < this.additionalPrice + this.payedPrice) {
                this.orderState = OrderState.REFUND_NEEDED;
            } else if (this.totalPrice == this.additionalPrice + this.payedPrice) {
                this.orderState = OrderState.COMPLETED;
            } else {
                this.orderState = OrderState.PAYMENT_NEEDED;
            }
        }
    }

    public void updateTravelerCount(int newAdultCount, int newChildCount, int newInfantCount) {
        this.adultCount = newAdultCount;
        this.childCount = newChildCount;
        this.infantCount = newInfantCount;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
