package com.uriel.travel.service.Login;

import com.nimbusds.jose.shaded.gson.Gson;
import com.uriel.travel.Base.GsonLocalDateTimeAdapter;
import com.uriel.travel.domain.SocialType;
import com.uriel.travel.dto.SocialLogin.SocialLoginResponseDto;
import com.uriel.travel.dto.SocialLogin.SocialTokenResponseDto;
import com.uriel.travel.dto.UserResponseDto;
import com.uriel.travel.exception.CustomBadRequestException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.exception.feign.KakaoApi;
import com.uriel.travel.exception.feign.KakaoUserApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Qualifier("kakaoLogin")
public class KaKaoLoginService implements SocialLoginService {
    @Value("${kakao.native}")
    private String KAKAO_NATIVE;
    @Value("${kakao.admin}")
    private String KAKAO_ADMIN;
    @Value("${kakao.apiKey}")
    private String KAKAO_API_KEY;
    @Value("${kakao.redirectUrl}")
    private String KAKAO_REDIRECT_URL;
    @Value("${kakao.secret}")
    private String KAKAO_SECRET;
    private final String GRANT_TYPE = "authorization_code";
    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private final KakaoApi kakaoApi;
    private final KakaoUserApi kakaoUserApi;
    final SocialType socialType = SocialType.KAKAO;

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }

    public SocialTokenResponseDto getAccessToken(String code) {
        if (code == null) throw new CustomBadRequestException(ErrorCode.BAD_AUTHORIZATION_CODE);
        ResponseEntity<?> response = kakaoApi.getAccessToken(
                GRANT_TYPE,
                KAKAO_API_KEY, //client_id
                KAKAO_REDIRECT_URL,
                KAKAO_SECRET,
                code
        );

        return new Gson() // json으로 받은 데이터를 내가 만든 객체에 자동으로 set
                .fromJson(
                        String.valueOf(response.getBody()),
                        SocialTokenResponseDto.class
                );
    }
    public UserResponseDto.NaverUser getUserInfo(String accessToken){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("authorization", "Bearer "+ accessToken); //헤더 생성
        ResponseEntity<?> response = kakaoUserApi.getUserInfo(headerMap);

        String jsonString = Objects.requireNonNull(response.getBody()).toString();
        Gson gson = new Gson();
        gson.newBuilder().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class,new GsonLocalDateTimeAdapter())
                .create();

        SocialLoginResponseDto.KakaoDto kakaoDto = gson.fromJson(jsonString,SocialLoginResponseDto.KakaoDto.class);
        return UserResponseDto.KakaoUser.of(kakaoDto);
    }

}
