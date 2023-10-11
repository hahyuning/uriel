package com.uriel.travel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public enum ReservationStatus {
    PENDING("입금 대기중"),
    COMPLETED("입금 완료"),
    CANCELED("예약 취소");
    private final String message;
}
