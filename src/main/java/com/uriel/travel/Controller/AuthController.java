package com.uriel.travel.Controller;


import com.fasterxml.jackson.databind.ser.Serializers;
import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.dto.UserRequestDto;
import com.uriel.travel.dto.UserResponseDto;
import com.uriel.travel.jwt.entity.TokenResponseDto;
import com.uriel.travel.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {
    private final AuthService authService;
    //회원가입
    @PostMapping("/signup")
    public BaseResponse<UserResponseDto.SignUp> signUp(@RequestBody UserRequestDto.SignUp userRequestDto) {
        return BaseResponse.ok(authService.signUp(userRequestDto));
    }
    //로그인
    @PostMapping("/login")
    public BaseResponse<TokenResponseDto> login(@RequestBody UserRequestDto.login userRequestDto){
        return BaseResponse.ok(authService.login(userRequestDto));
    }
    //로그아웃
    @PostMapping("/logout")
    public BaseResponse<Void> login(HttpServletRequest request){
        authService.logout(request);
        return BaseResponse.ok();
    }

    //회원정보 조회
//    @GetMapping("/mypage")
//    public BaseResponse<UserResponseDto.Profile> getUserProfile(){
//    }
}
