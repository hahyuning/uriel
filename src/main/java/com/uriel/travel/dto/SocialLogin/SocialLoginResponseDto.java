package com.uriel.travel.dto.SocialLogin;

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



}
