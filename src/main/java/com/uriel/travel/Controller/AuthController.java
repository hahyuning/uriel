package com.uriel.travel.Controller;


import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.user.UserRequestDto;
import com.uriel.travel.jwt.JwtTokenProvider;
import com.uriel.travel.service.CustomUserDetailsService;
import com.uriel.travel.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUsername();
    }

    @PostMapping("/signup")
    public BaseResponse<Void> signup(@RequestBody UserRequestDto.SignUp userRequestDto) {
        customUserDetailsService.signUp(userRequestDto);
        return BaseResponse.ok();
    }

    @PatchMapping("/logout")
    public BaseResponse<Void> logout(HttpServletRequest request) {
        String encryptedRefreshToken = jwtTokenProvider.resolveRefreshToken(request);
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        customUserDetailsService.logout(encryptedRefreshToken, accessToken);

        return BaseResponse.ok();
    }

    @PatchMapping("/reissue")
    public BaseResponse<Void> reissue(HttpServletRequest request,
                                      HttpServletResponse response) {

        String encryptedRefreshToken = jwtTokenProvider.resolveRefreshToken(request);
        String newAccessToken = customUserDetailsService.reissueAccessToken(encryptedRefreshToken);
        response.setHeader("Authorization", newAccessToken);

        return BaseResponse.ok();
    }
}
