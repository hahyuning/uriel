package com.uriel.travel.jwt.dto;

import lombok.Builder;

@Builder
public class TokenResponseDto {
    private String grantType; //Bearer
    private String accessToken;
    private String refreshToken;

}
