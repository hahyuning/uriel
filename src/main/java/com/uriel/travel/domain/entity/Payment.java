package com.uriel.travel.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Payment {

    @Id
    String paymentKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    Order order;

    String mid;

    String method;

    int totalAmount;

    int balanceAmount;

    String status;

    LocalDateTime requestedAt;

    LocalDateTime approvedAt;

    String lastTransactionKey;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Cancel> cancels = new ArrayList<>();

    public void setOrder(Order order) {
        this.order = order;
        order.getPayments().add(this);
    }

}
