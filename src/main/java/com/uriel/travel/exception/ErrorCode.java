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
    DUPLICATE_PHONE_NUMBER(40001,"전화번호가 중복되었습니다."),
    DUPLICATE_EMAIL(40002,"이미 존재하는 이메일입니다."),
    LOGIN_EMAIL_ERROR(40003,"사용자 이메일이 존재하지 않습니다."),
    LOGIN_PASSWORD_ERROR(40004,"비밀번호 오류입니다."),
    DUPLICATE_UNIQUE_KEY(40005,"무결성 제약조건에 위배되었습니다."),
    DUPLICATE_SOCIAL_ID(40007,"이미 소셜 로그인을 등록하셨습니다."),
    BAD_AUTHORIZATION_CODE(40006,"fail to load authorization code"),
    DUPLICATE_COUNTRY(40007, "이미 존재하는 여행지입니다."),
    DUPLICATE_TAG(40008, "이미 존재하는 태그입니다."),
    BAD_TEG_TYPE(40009, "태그 타입이 올바르지 않습니다."),
    BAD_SOCIAL_TYPE(40010, "유효하지 않은 소셜 타입입니다."),
    EXCEED_MAX_COUNT(40011, "예약 가능한 인원을 초과했습니다."),

    /**
     * 401 : 인증되지 않은 사용자
     */
    EXPIRED_TOKEN(40101, "토큰 유효 시간이 만료되었습니다"),
    INVALID_TOKEN(40102, "유효하지 않은 토큰입니다."),
    LOGIN_REQUIRED(40103, "토큰이 존재하지 않습니다. 로그인 이후 요청해주세요"),
    REFRESH_TOKEN_NOT_EXIST(40104, "리프레쉬 토큰이 존재하지 않습니다"),
    REFRESH_TOKEN_NULL(40105,"리프레쉬 토큰 값이 비었습니다."),
    MEMBER_ROLE_INVALID(40106,"MEMBER_ROLE_INVALID"),

    /**
     * 403: 권한 없음
     */
    FORBIDDEN(403, "권한이 없습니다."),

    /**
     * 404: NOT FOUND
     */
    NOT_FOUND(404, "요청한 자원을 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(40401, "존재하지 않는 사용자 정보입니다"),
    NOT_FOUND_REVIEW(40402, "존재하지 않는 리뷰입니다"),
    NOT_FOUND_ORDER(40404, "존재하지 않는 주문 번호입니다."),
    NOT_FOUND_PRODUCT(40405,"상품을 찾을 수 없습니다."),
    NOT_FOUND_COUNTRY(40406, "여행지를 찾을 수 없습니다."),
    NOT_FOUND_PACKAGE(40407, "패키지를 찾을 수 없습니다."),
    NOT_FOUND_TAG(40408, "해당 태그가 존재하지 않습니다."),

    /**
     * 500 서버에러
     */
    INTERNAL_SERVER_ERROR(500, "서버 오류입니다."),
    OPEN_API_ERROR(50001, "오픈 API 호출에 오류가 있습니다.");

    private final int code;
    private final String message;
}

