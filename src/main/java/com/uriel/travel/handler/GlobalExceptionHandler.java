package com.uriel.travel.handler;


import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //400
    @ExceptionHandler(CustomBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> customBadRequestHandler(CustomBadRequestException e) {
        return BaseResponse.fail(e.getErrorCode());
    }
    //sql 무결성 제약조건 위배 예외
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> duplicateUniqueKeyHandler(SQLIntegrityConstraintViolationException e) {
        return BaseResponse.fail(ErrorCode.DUPLICATE_UNIQUE_KEY);
    }
    //404: 데이터 없음
    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<?> customNotFoundHandler(CustomNotFoundException e) {
        return BaseResponse.fail(e.getErrorCode());
    }
    //401: 인증되지 않은 사용자
    @ExceptionHandler(CustomUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<?> customUnauthorizedHandler(CustomUnauthorizedException e) {
        return BaseResponse.fail(e.getErrorCode());
    }
    //403 권한 없음
    @ExceptionHandler({CustomForbiddenException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse<?> customForbiddenHandler(CustomBadRequestException e) {
        return BaseResponse.fail(e.getErrorCode());
    }
}
