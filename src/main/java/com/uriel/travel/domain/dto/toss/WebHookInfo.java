package com.uriel.travel.domain.dto.toss;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


public class WebHookInfo {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class VirtualAccount {
        LocalDateTime createdAt; // 웹훅 생성 시간
        String secret; // 가상계좌 웹훅 요청이 정상적인 요청인지 검증하는 값, 결제 승인 api 의 응답으로 돌아온 secret 값과 같은지 확인
        String status; // 결제 상태
        String transactionKey; // 상태가 변경된 가상계좌 거래를 특정하는 키
        String orderId; // 주문 id
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OtherPayments {
        LocalDateTime createdAt; // 웹훅 생성 시간
        Payment data;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Payment {
        String orderId;
        String status;
        String approvedAt;
        Long totalAmount;
        Cancel cancel;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Cancel {
        Long cancelAmount;
    }
}
