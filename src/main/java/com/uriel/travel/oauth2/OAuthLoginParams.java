package com.uriel.travel.oauth2;

import com.uriel.travel.domain.SocialType;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {

    SocialType oAuthProvider();
    MultiValueMap<String, String> makeBody();
}
