//package com.uriel.travel.handler;
//
//import com.uriel.travel.domain.Role;
//import com.uriel.travel.domain.entity.User;
//import com.uriel.travel.jwt.JwtTokenProvider;
//import com.uriel.travel.oauth2.CustomOAuth2User;
//import com.uriel.travel.service.CustomUserDetailsService;
//import com.uriel.travel.service.UserService;
//import com.uriel.travel.util.CustomAuthUtil;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.io.IOException;
//import java.net.URI;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final UserService userService;
//
////    @Override
////    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
////        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
////
////        String email = oAuth2User.getEmail(); // OAuth2User로부터 Resource Owner의 이메일 주소를 얻음 객체로부터
////        List<String> authorities = CustomAuthUtil.createAuthorities(Role.ROLE_GUEST.toString());           // 권한 정보 생성
////
////        redirect(request, response, email, authorities);  // Access Token과 Refresh Token을 Frontend에 전달하기 위해 Redirect
////    }
////
////    private void redirect(HttpServletRequest request, HttpServletResponse response, String email, List<String> authorities) throws IOException {
////        log.info("Token 생성 시작");
////        String accessToken = delegateAccessToken(email, authorities);  // Access Token 생성
////        String refreshToken = delegateRefreshToken(email);     // Refresh Token 생성
////        User user = userService.loadUserByUsername(email);
////        Long userId = user.getId();
////        String username = user.getUsername();
////        user.setRefreshToken(refreshToken);
////        userService.saveUser(user);
////
////        String uri = createURI(accessToken, refreshToken, userId, username).toString();   // Access Token과 Refresh Token을 포함한 URL을 생성
////        getRedirectStrategy().sendRedirect(request, response, uri);   // sendRedirect() 메서드를 이용해 Frontend 애플리케이션 쪽으로 리다이렉트
////    }
////
////    // Access Token 생성
////    private String delegateAccessToken(String username, List<String> authorities) {
////        Map<String, Object> claims = new HashMap<>();
////        claims.put("username", username);
////        claims.put("roles", authorities);
////
////        String subject = username;
////        Date expiration = jwtTokenProvider.getTokenExpiration(jwtTokenProvider.getAccessTokenExpirationMinutes());
////        String base64EncodedSecretKey = jwtTokenProvider.encodeBase64SecretKey(jwtTokenProvider.getSecretKey());
////
////        String accessToken = jwtTokenProvider.createToken(claims, subject, expiration, base64EncodedSecretKey);
////
////        return accessToken;
////    }
////
////    // Refresh Token 생성
////    private String delegateRefreshToken(String username) {
////        String subject = username;
////        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
////        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
////
////        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
////
////        return refreshToken;
////    }
////
////    // Redirect URI 생성. JWT를 쿼리 파라미터로 담아 전달한다.
////    private URI createURI(String accessToken, String refreshToken, Long userId, String username) {
////        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
////        queryParams.add("user_id", String.valueOf(userId));
////        queryParams.add("username", username);
////        queryParams.add("access_token", accessToken);
////        queryParams.add("refresh_token", refreshToken);
////
////        return UriComponentsBuilder
////                .newInstance()
////                .scheme("https")
////                .host("66challenge.shop")
////                .path("/oauth")
////                .queryParams(queryParams)
////                .build()
////                .toUri();
////    }
//}
