package com.uriel.travel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * 200: 성공 코드
     */
    OK(200, "요청이 정상적으로 수행되었습니다."),

    /**
     * 400: BAD REQUEST
     */
    BAD_REQUEST(400,"잘못된 요청입니다."),
    DUPLICATE_EMAIL(40001,"이미 존재하는 이메일입니다."),
    LOGIN_EMAIL_ERROR(40002,"사용자 이메일이 존재하지 않습니다."),
    LOGIN_PASSWORD_ERROR(40003,"비밀번호가 일치하지 않습니다."),
    DUPLICATE_UNIQUE_KEY(40004,"무결성 제약조건에 위배되었습니다."),
    DUPLICATE_SOCIAL_ID(40005,"이미 소셜 로그인을 등록하셨습니다."),
    BAD_AUTHORIZATION_CODE(40006,"인증 코드가 유효하지 않습니다."),
    EXCEED_MAX_COUNT(40007, "예약 가능한 인원을 초과했습니다."),
    RESERVATION_NOT_AVAILABLE(40008, "예약 마감된 상품입니다."),
    PAYMENT_FAILURE(40009, "결제에 실패했습니다."),
    DUPLICATE_TAG(40010, "해당 태그가 이미 존재합니다."),

    /**
     * 401 : 인증되지 않은 사용자
     */
    EXPIRED_TOKEN(40101, "토큰 유효 시간이 만료되었습니다."),
    INVALID_TOKEN(40102, "유효하지 않은 토큰입니다."),
    LOGIN_REQUIRED(40103, "토큰이 존재하지 않습니다. 로그인 이후 요청해주세요."),
    REFRESH_TOKEN_NOT_EXIST(40104, "리프레쉬 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_NULL(40105,"리프레쉬 토큰 값이 비었습니다."),
    MEMBER_ROLE_INVALID(40106,"사용자 역할이 유효하지 않습니다."),

    /**
     * 403: 권한 없음
     */
    FORBIDDEN(403, "권한이 없습니다."),

    /**
     * 404: NOT FOUND
     */
    NOT_FOUND(404, "요청한 자원을 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(40401, "존재하지 않는 사용자 정보입니다"),
    NOT_FOUND_ORDER(40402, "존재하지 않는 주문 번호입니다."),
    NOT_FOUND_PRODUCT(40403,"상품을 찾을 수 없습니다."),
    NOT_FOUND_COUNTRY(40404, "여행지를 찾을 수 없습니다."),
    NOT_FOUND_PACKAGE(40405, "패키지를 찾을 수 없습니다."),
    NOT_FOUND_TAG(40406, "해당 태그가 존재하지 않습니다."),

    /**
     * 500 서버에러
     */
    INTERNAL_SERVER_ERROR(500, "서버 오류입니다."),
    OPEN_API_ERROR(50001, "오픈 API 호출에 오류가 있습니다.");

    private final int code;
    private final String message;
}

