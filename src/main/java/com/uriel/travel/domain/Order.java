package com.uriel.travel.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="order")
public class Order extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDateTime createDate;
    LocalDateTime modifiedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    Users user;
    @OneToMany(mappedBy = "product")
    List<Product> productList;
    int totalPrice;
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

}
