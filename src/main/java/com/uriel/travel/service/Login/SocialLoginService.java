package com.uriel.travel.service.Login;

import com.uriel.travel.domain.SocialType;
import com.uriel.travel.domain.dto.socialLogin.SocialTokenResponseDto;
import com.uriel.travel.domain.dto.user.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {
    SocialType getSocialType();
    SocialTokenResponseDto getAccessToken(String code);

    UserResponseDto.NaverUser getUserInfo(String accessToken);
}
