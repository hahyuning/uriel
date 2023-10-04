package com.uriel.travel.exception;

import lombok.Getter;

@Getter
public class CustomForbiddenException extends CustomException{
    public CustomForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
