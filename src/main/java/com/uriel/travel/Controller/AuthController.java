package com.uriel.travel.Controller;


import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.dto.NaverDto;
import com.uriel.travel.dto.UserRequestDto;
import com.uriel.travel.dto.UserResponseDto;
import com.uriel.travel.jwt.entity.TokenResponseDto;
import com.uriel.travel.service.AuthService;
import com.uriel.travel.service.SocialLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {
    private final AuthService authService;
    private final SocialLoginService socialLoginService;
    //회원가입
    @PostMapping("/users/signup")
    public BaseResponse<UserResponseDto.SignUp> signUp(@RequestBody UserRequestDto.SignUp userRequestDto) {
        return BaseResponse.ok(authService.signUp(userRequestDto));
    }
    //로그인
    @PostMapping("/users/login")
    public BaseResponse<TokenResponseDto> login(@RequestBody UserRequestDto.login userRequestDto){
        return BaseResponse.ok(authService.login(userRequestDto));
    }
    @GetMapping("/callback")
    public BaseResponse<Void> callback(HttpServletRequest request) throws Exception {
        NaverDto naverInfo = socialLoginService.getNaverInfo(request.getParameter("code"));
        return BaseResponse.ok();
    }

    //todo: 로그아웃 수정
//    @PostMapping("/logout")
//    public BaseResponse<Void> logout(HttpServletRequest request){
//        authService.logout(request);
//        return BaseResponse.ok();
//    }
    //Todo: refresh token 재발급 추가

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
