package com.uriel.travel.feign;

import com.uriel.travel.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "kakaoAuth",url = "https://kauth.kakao.com", configuration = {FeignConfiguration.class} )
public interface KakaoApi {
    @GetMapping("/oauth2.0/token")
    ResponseEntity<String> getAccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_url") String redirectUrl,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code
    );
}
