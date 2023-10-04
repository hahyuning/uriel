package com.uriel.travel.exception;

import lombok.Getter;

@Getter
public class CustomNotFoundException extends CustomException{
    public CustomNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
