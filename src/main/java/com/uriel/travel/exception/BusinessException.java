package com.uriel.travel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BusinessException extends RuntimeException{
    ErrorCode errorCode;
}
