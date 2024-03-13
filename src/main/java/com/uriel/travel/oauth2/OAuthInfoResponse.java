package com.uriel.travel.oauth2;

import com.uriel.travel.domain.SocialType;

import java.time.LocalDate;

public interface OAuthInfoResponse {

    String getEmail();
    String getName();
    String getGender();
    String getBirthday();
    String getBirthyear();
    LocalDate getBirth();
    String getPhoneNumber();
    SocialType getOAuthProvider();
}
