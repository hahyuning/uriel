package com.uriel.travel.service.Login;

import com.uriel.travel.domain.SocialType;
import com.uriel.travel.dto.SocialLogin.SocialTokenResponseDto;
import com.uriel.travel.dto.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {
    SocialType getSocialType();
    SocialTokenResponseDto getAccessToken(String code);

    UserResponseDto.NaverUser getUserInfo(String accessToken);
}
