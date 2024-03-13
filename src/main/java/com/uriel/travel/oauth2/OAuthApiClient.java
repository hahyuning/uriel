package com.uriel.travel.oauth2;

import com.uriel.travel.domain.SocialType;

public interface OAuthApiClient {

    SocialType oAuthProvider();
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);
}
