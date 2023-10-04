package com.uriel.travel.Controller;


import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.dto.UserRequestDto;
import com.uriel.travel.dto.UserResponseDto;
import com.uriel.travel.jwt.entity.TokenResponseDto;
import com.uriel.travel.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public BaseResponse<UserResponseDto.SignUp> signUp(@RequestBody UserRequestDto.SignUp userRequestDto) {
        return BaseResponse.ok(authService.signUp(userRequestDto));
    }
    @PostMapping("/login")
    public BaseResponse<TokenResponseDto> login(@RequestBody UserRequestDto.login userRequestDto){
        return BaseResponse.ok(authService.login(userRequestDto));
    }
}
