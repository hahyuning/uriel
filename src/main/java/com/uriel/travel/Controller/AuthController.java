package com.uriel.travel.Controller;


import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.SocialType;
import com.uriel.travel.dto.UserRequestDto;
import com.uriel.travel.dto.UserResponseDto;
import com.uriel.travel.jwt.domain.TokenResponseDto;
import com.uriel.travel.service.Login.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {
    private final AuthService authService;

    //회원가입
    @PostMapping("/users/signup")
    public BaseResponse<UserResponseDto.SignUp> signUp(@RequestBody UserRequestDto.SignUp userRequestDto) {
        return BaseResponse.ok(authService.signUp(userRequestDto));
    }
    //로그인
//    @PostMapping("/users/login")
//    public BaseResponse<TokenResponseDto> login(@RequestBody UserRequestDto.login userRequestDto){
//        return BaseResponse.ok(authService.login(userRequestDto));
//    }
    @GetMapping("/oauth/naver")
    public BaseResponse<TokenResponseDto> naverLogin(HttpServletRequest request) {
        return BaseResponse.ok(authService.doSocialLogin(request.getParameter("code"),SocialType.NAVER));
    }
    @GetMapping("/oauth/kakao")
    public BaseResponse<TokenResponseDto> kakaoLogin(HttpServletRequest request) {
        return BaseResponse.ok(authService.doSocialLogin(request.getParameter("code"),SocialType.KAKAO));
    }


//    //todo: 로그아웃 수정
//    @PostMapping("/auth/logout")
//    public BaseResponse<Void> logout(HttpServletRequest request){
//        String encryptedRefreshToken = jwtTokenProvider.resolveRefreshToken(request);
//        String accessToken = jwtTokenProvider.resolveAccessToken(request);
//        authService.logout(encryptedRefreshToken, accessToken);
//        return BaseResponse.ok();
//    }

    //Todo: refresh token 재발급 추가
    @PostMapping("/kakao/logout")
    public BaseResponse<Long> kakaoLogout(HttpServletRequest request){
        String access_token = (String)request.getHeader("access_token");
        return BaseResponse.ok(authService.kakaoLogout(access_token));
    }
    //회원정보 조회
    @GetMapping("/users/my-page")
    public BaseResponse<UserResponseDto.Profile> getUserProfile(){
        return BaseResponse.ok(authService.getUserProfile());
    }
    //회원정보 수정
    @PutMapping ("/users/my-page")
    public BaseResponse<Void> updateUserProfile(@RequestBody UserRequestDto.Profile requestDto){
        authService.updateUserProfile(requestDto);
        return BaseResponse.ok();
    }
    @PostMapping("/users/find-id")
    public BaseResponse<UserResponseDto.FindId> findId(@RequestBody UserRequestDto.FindId requestDto){
        return BaseResponse.ok(authService.findId(requestDto));
    }
    @PostMapping("/users/find-pw")
    public BaseResponse<Void> findPw(@RequestBody UserRequestDto.FindPw requestDto){
        authService.findPw(requestDto);
        return BaseResponse.ok();
    }

}
