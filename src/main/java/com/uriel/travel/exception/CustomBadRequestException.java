package com.uriel.travel.exception;

import lombok.Getter;

@Getter
public class CustomBadRequestException extends CustomException{
    public CustomBadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
