package com.uriel.travel.domain.entity;

import com.uriel.travel.domain.Airline;
import com.uriel.travel.domain.ProductState;
import com.uriel.travel.domain.Release;
import com.uriel.travel.domain.SaveState;
import com.uriel.travel.domain.dto.product.ProductRequestDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    Release isPublic; // 공개여부

    SaveState saveState; // 공개여부

    String productCode; // 상품코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    Package aPackage;

    LocalDateTime startDate; // 출발일시
    LocalDateTime endDate; // 도착일시

    int minCount; // 최소 출발인원
    int maxCount; // 최대 예약인원

    @Builder.Default
    int nowCount = 0; // 현재 예약인원

    ProductState productState; // 상품 상태

    Airline airline; // 항공사

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
        this.airline = Airline.from(requestDto.getAirline());
        this.price = requestDto.getPrice();
    }

    // id 초기화
    public void idInitialize() {
        this.id = null;
    }

    public void setPrivacy(String privacy) {
        this.isPublic = Release.from(privacy);
    }

    public void setSaveState(SaveState saveState) {
        this.saveState = saveState;
    }


    public void setProductCode() {
        String resultCode = this.aPackage.getCountry().toString(); // 여행지역 코드
        resultCode += LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")); // 생성 년월일
        resultCode += String.format("%02d", aPackage.getId()) + String.format("%02d", this.id);
        resultCode += this.airline.getCode();

        this.productCode = resultCode;
    }
}
