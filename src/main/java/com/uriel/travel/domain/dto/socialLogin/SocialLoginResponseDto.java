package com.uriel.travel.domain.dto.socialLogin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public class SocialLoginResponseDto{
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NaverDto {

        String resultCode;
        String message;
        @Builder.Default
        private Response response = Response.builder().build();
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Response{
            private String id;
            private String name;
            private String email;
            private String gender;
            private String birthday;
            private String birthyear;
            private String mobile;
        }

    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoDto {
        private Long id;
        @Builder.Default
        private KakaoAccount kakao_account = KakaoAccount.builder().build();
        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class KakaoAccount{
            private String email;
            private Boolean has_email;
            private Boolean is_email_vaild;
            private String name;
            private String gender;
            private String birthday;
            private String birthyear;
            private String phone_number;
            @Builder.Default
            private KakaoProfile kakaoProfile = KakaoProfile.builder().build();
            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            public static class KakaoProfile{
                String nickname;
            }

        }


    }



}
