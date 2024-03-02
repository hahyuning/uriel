package com.uriel.travel.domain.dto.payment;

import com.uriel.travel.domain.entity.Payment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentInfo {

    String paymentKey;

    String mid;

    String method;

    int totalAmount;

    int balanceAmount;

    String status;

    LocalDateTime requestedAt;

    LocalDateTime approvedAt;

    String lastTransactionKey;

    public Payment toEntity() {
        return Payment
                .builder()
                .paymentKey(this.paymentKey)
                .mid(this.mid)
                .method(this.method)
                .totalAmount(this.totalAmount)
                .balanceAmount(this.balanceAmount)
                .status(this.status)
                .requestedAt(this.requestedAt)
                .approvedAt(this.approvedAt)
                .lastTransactionKey(lastTransactionKey)
                .build();
    }
}
