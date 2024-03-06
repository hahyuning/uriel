package com.uriel.travel.domain.dto.user;

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
    Long accessTokenExpiresIn;
}
