package com.uriel.travel.oauth2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uriel.travel.domain.SocialType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {
    // Access Token 으로 요청한 외부 API 프로필 응답값

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private String name;
        private String email;
        private String gender;
        private String birthday;
        private String birthyear;
        private String phoneNumber;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getName() {
        return kakaoAccount.name;
    }

    @Override
    public String getGender() {
        return kakaoAccount.gender;
    }

    @Override
    public String getBirthday() {
        return kakaoAccount.birthday;
    }

    @Override
    public String getBirthyear() {
        return kakaoAccount.birthyear;
    }

    @Override
    public String getPhoneNumber() {
        return kakaoAccount.phoneNumber;
    }

    @Override
    public LocalDate getBirth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        if (kakaoAccount.birthday != null && kakaoAccount.birthyear != null) {
            return LocalDate.parse(kakaoAccount.birthyear + kakaoAccount.birthday, formatter);
        }
        return null;
    }

    @Override
    public SocialType getOAuthProvider() {
        return SocialType.KAKAO;
    }
}
