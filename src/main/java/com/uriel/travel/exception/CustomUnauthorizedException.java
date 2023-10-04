package com.uriel.travel.exception;

import lombok.Getter;

@Getter
public class CustomUnauthorizedException extends CustomException{
    public CustomUnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
