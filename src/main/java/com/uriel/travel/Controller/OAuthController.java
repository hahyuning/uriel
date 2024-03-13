package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.user.UserRequestDto;
import com.uriel.travel.domain.dto.user.UserResponseDto;
import com.uriel.travel.jwt.JwtToken;
import com.uriel.travel.oauth2.KakaoLoginParams;
import com.uriel.travel.oauth2.NaverLoginParams;
import com.uriel.travel.service.OAuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthLoginService oAuthLoginService;

//    @PostMapping("/kakao")
//    public ResponseEntity<JwtToken> loginKakao(@RequestBody KakaoLoginParams params) {
//        return ResponseEntity.ok(oAuthLoginService.login(params));
//    }
//
//    @PostMapping("/naver")
//    public ResponseEntity<JwtToken> loginNaver(@RequestBody NaverLoginParams params) {
//        return ResponseEntity.ok(oAuthLoginService.login(params));
//    }

    @PostMapping("/kakao")
    public BaseResponse<UserResponseDto.SocialUserInfo> loginKakao(@RequestBody KakaoLoginParams params) {
        return BaseResponse.ok(oAuthLoginService.login(params));
    }

    @PostMapping("/naver")
    public BaseResponse<UserResponseDto.SocialUserInfo> loginNaver(@RequestBody NaverLoginParams params) {
        return BaseResponse.ok(oAuthLoginService.login(params));
    }

    // 회원 추가정보 입력
    @PostMapping("/update/info")
    private BaseResponse<JwtToken> updateSocialUserInfo(@RequestBody UserRequestDto.Update requestDto) {
        return BaseResponse.ok(oAuthLoginService.updateUserInfo(requestDto));
    }
}
