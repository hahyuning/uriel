package com.uriel.travel.Controller;


import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.user.TokenDto;
import com.uriel.travel.domain.dto.user.UserRequestDto;
import com.uriel.travel.service.CustomUserDetailsService;
import com.uriel.travel.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUsername();
    }

    @PostMapping("/signup")
    public BaseResponse<Void> signup(@RequestBody UserRequestDto.SignUp userRequestDto) {
        customUserDetailsService.signUp(userRequestDto);
        return BaseResponse.ok();
    }

    @PostMapping("/login")
    public BaseResponse<TokenDto> login(@RequestBody UserRequestDto.Login userRequestDto) {
        return BaseResponse.ok(customUserDetailsService.login(userRequestDto));
    }
}
