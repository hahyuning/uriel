package com.uriel.travel.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cancel {

    @Id
    String transactionKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_key")
    Payment payment;

    int cancelAmount;

    int refundableAmount;

    LocalDateTime canceledAt;

    public void setPayment(Payment payment) {
        this.payment = payment;
        payment.getCancels().add(this);
    }
}
