package com.uriel.travel.oauth2;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attributes {

    private Map<String, Object> attributes;     // OAuth2 반환하는 유저 정보
    private String nameAttributesKey;
    private String name;
    private String email;
    private String gender;
    private String birthday;
    private String birthyear;
    private String phone;

    @Builder
    public OAuth2Attributes(Map<String, Object> attributes, String nameAttributesKey,
                           String name, String email, String gender, String birthday, String birthyear, String phone) {
        this.attributes = attributes;
        this.nameAttributesKey = nameAttributesKey;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.birthyear = birthyear;
        this.phone = phone;
    }

    public static OAuth2Attributes of(String socialName, Map<String, Object> attributes) {
        if ("kakao".equals(socialName)) {
            return ofKakao("id", attributes);
        }  else if ("naver".equals(socialName)) {
            return ofNaver("id", attributes);
        }
        return null;
    }

    private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return OAuth2Attributes.builder()
                .name(String.valueOf(kakaoAccount.get("name")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .gender(String.valueOf(kakaoAccount.get("gender")))
                .birthday(String.valueOf(kakaoAccount.get("birthday")))
                .birthyear(String.valueOf(kakaoAccount.get("birthyear")))
                .phone(String.valueOf(kakaoAccount.get("phone_number")))
                .nameAttributesKey(userNameAttributeName)
                .attributes(attributes)
                .build();
    }

    public static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attributes.builder()
                .name(String.valueOf(response.get("name")))
                .email(String.valueOf(response.get("email")))
                .gender(String.valueOf(response.get("gender")))
                .birthday(String.valueOf(response.get("birthday")))
                .birthyear(String.valueOf(response.get("birthyear")))
                .phone(String.valueOf(response.get("mobile")))
                .attributes(response)
                .nameAttributesKey(userNameAttributeName)
                .build();
    }

//    public User toEntity() {
//        return User.builder()
//                .username(name)
//                .email(email)
//                .roles(List.of("USER"))
//                .build();
//    }
}
