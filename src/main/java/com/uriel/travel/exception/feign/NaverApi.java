package com.uriel.travel.exception.feign;

import com.uriel.travel.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "naverAuth",url = "https://nid.naver.com", configuration = {FeignConfiguration.class} )
public interface NaverApi {
    @GetMapping("/oauth2.0/token")
    ResponseEntity<String> getAccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("state") String state
    );
}
