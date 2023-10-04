package com.uriel.travel.jwt.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponseDto {
    private String grantType; //Bearer
    private String accessToken;
    private String refreshToken;


}
