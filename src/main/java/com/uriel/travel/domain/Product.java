package com.uriel.travel.domain;

import com.uriel.travel.dto.product.ProductRequestDto;
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
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    Release isPublic = Release.TEMPORARY; // 공개여부

    @Builder.Default
//    @Column(unique = true)
    String productCode = ""; // 상품코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    Package aPackage;

    LocalDateTime startDate; // 출발일시
    LocalDateTime endDate; // 도착일시

    int minCount; // 최소 출발인원
    int maxCount; // 최대 예약인원

    @Builder.Default
    int nowCount = 0; // 현재 예약인원

    @Enumerated(EnumType.STRING)
    @Builder.Default
    ProductState productState = ProductState.RESERVATION_AVAILABLE; // 상품 상태

    String airline; // 항공사

    int price; // 가격

    // 연관관계
    public void setPackage(Package aPackage) {
        this.aPackage = aPackage;
        aPackage.getProductList().add(this);
    }

    // 상품 수정
    public void update(ProductRequestDto.Update requestDto) {
        this.isPublic = Release.from(requestDto.getPrivacy());
        this.startDate = requestDto.getStartDate();
        this.endDate = requestDto.getEndDate();
        this.minCount = requestDto.getMinCount();
        this.maxCount = requestDto.getMaxCount();
        this.productState = ProductState.from(requestDto.getProductState());
        this.airline = requestDto.getAirline();
        this.price = requestDto.getPrice();
    }

    // id 초기화
    public void idInitialize() {
        this.id = null;
    }

    public void setPrivacy(String privacy) {
        this.isPublic = Release.from(privacy);
    }
}
