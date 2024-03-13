package com.uriel.travel.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.user.UserRequestDto;
import com.uriel.travel.domain.entity.User;
import com.uriel.travel.service.RedisService;
import com.uriel.travel.util.Aes256Util;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // ServletInputStream을 LoginDto 객체로 역직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        UserRequestDto.Login loginDto = objectMapper.readValue(request.getInputStream(), UserRequestDto.Login.class);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();
        JwtToken jwtToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());
        String accessToken = jwtToken.getAccessToken();
        String refreshToken = jwtToken.getRefreshToken();
        String encryptedRefreshToken = Aes256Util.encrypt(refreshToken);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", encryptedRefreshToken);

        // 로그인 성공시 Refresh Token Redis 저장 ( key = Email / value = Refresh Token )
        long refreshTokenExpirationMillis = jwtTokenProvider.getRefreshTokenExpirationMillis();
        redisService.setValues(user.getEmail(), refreshToken, Duration.ofMillis(refreshTokenExpirationMillis));

        sendResponse(response, jwtToken);
    }

    public <T> void sendResponse(HttpServletResponse response, T data) throws IOException {
        BaseResponse<T> httpResponse = BaseResponse.ok(data);
        String json = objectMapper.writeValueAsString(httpResponse);
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
    }
}
