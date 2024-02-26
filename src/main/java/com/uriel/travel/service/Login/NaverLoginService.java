package com.uriel.travel.service.Login;

import com.nimbusds.jose.shaded.gson.Gson;
import com.uriel.travel.Base.GsonLocalDateTimeAdapter;
import com.uriel.travel.domain.SocialType;
import com.uriel.travel.domain.dto.socialLogin.SocialLoginResponseDto;
import com.uriel.travel.domain.dto.socialLogin.SocialTokenResponseDto;
import com.uriel.travel.domain.dto.user.UserResponseDto;
import com.uriel.travel.exception.CustomBadRequestException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.exception.feign.NaverApi;
import com.uriel.travel.exception.feign.NaverUserApi;
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
@Qualifier("naverLogin")
public class NaverLoginService implements SocialLoginService {
    @Value("${naver.clientId}")
    private String NAVER_CLIENT_ID;
    @Value("${naver.redirectUrl}")
    private String NAVER_REDIRECT_URL ;
    @Value("${naver.secret}")
    private String NAVER_CLIENT_SECRET;
    private final static String NAVER_AUTH_URI = "https://nid.naver.com";
    private final static String NAVER_API_URI = "https://openapi.naver.com";
    private final String GRANT_TYPE = "authorization_code";
    private final NaverApi naverApi;
    private final NaverUserApi naverUserApi;
    final SocialType socialType = SocialType.NAVER;

    @Override
    public SocialType getSocialType() {
        return SocialType.NAVER;
    }

    public SocialTokenResponseDto getAccessToken(String code) {
        if (code == null) throw new CustomBadRequestException(ErrorCode.BAD_AUTHORIZATION_CODE);
        ResponseEntity<?> response = naverApi.getAccessToken(
                GRANT_TYPE,
                NAVER_CLIENT_ID,
                NAVER_CLIENT_SECRET,
                code,
                "state"
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
        ResponseEntity<?> response = naverUserApi.getUserInfo(headerMap);

        String jsonString = Objects.requireNonNull(response.getBody()).toString();
        Gson gson = new Gson();
        gson.newBuilder().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class,new GsonLocalDateTimeAdapter())
                .create();

        SocialLoginResponseDto.NaverDto naverDto = gson.fromJson(jsonString,SocialLoginResponseDto.NaverDto.class);
        SocialLoginResponseDto.NaverDto.Response naverUserInfo = naverDto.getResponse();
        return UserResponseDto.NaverUser.of(naverUserInfo);
    }

}
