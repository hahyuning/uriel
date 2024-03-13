package com.uriel.travel.jwt;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtToken {

    String accessToken;
    String refreshToken;
    String grantType;
    Long expiresIn;
}
