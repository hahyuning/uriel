package com.uriel.travel.jwt;

import com.uriel.travel.domain.Role;
import com.uriel.travel.domain.dto.user.JwtToken;
import com.uriel.travel.domain.entity.User;
import com.uriel.travel.util.Aes256Util;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private String AUTHORITIES_KEY = "auth";
    private String BEARER_TYPE = "Bearer";
    private long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";

    private Key key;

    // Bean 등록후 Key SecretKey HS256 decode
    @PostConstruct
    public void init() {
        String base64EncodedSecretKey = encodeBase64SecretKey(this.secretKey);
        this.key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);
    }

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtToken createToken(String email, String role) {
        String encryptedEmail = Aes256Util.encrypt(email);

        Claims claims = Jwts.claims().setSubject(encryptedEmail);
        claims.put("role", role);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME).getTime()).build();
    }

    public String getSubject(Claims claims) {
        return Aes256Util.decrypt(claims.getSubject());
    }

    public Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        } catch (Exception e) {
            throw new BadCredentialsException("유효한 토큰이 아닙니다.");
        }
        return claims;
    }

    // Request Header에 Access Token 정보를 추출하는 메서드
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Request Header에 Refresh Token 정보를 추출하는 메서드
    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        return null;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaims(accessToken);
        String email = getSubject(claims);

        User user = User.builder()
                .email(email)
                .role(Role.from(claims.get("role").toString()))
                .build();

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    // 토큰 검증
    public boolean validateToken(String token, HttpServletResponse response) {
        try {
            getClaims(token);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token");
            log.trace("Invalid JWT token trace = {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
            log.trace("Expired JWT token trace = {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token");
            log.trace("Unsupported JWT token trace = {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.");
            log.trace("JWT claims string is empty trace = {}", e);
        }
        return true;
    }

    public long getAccessTokenExpirationMillis() {
        Date date = new Date();
        return new Date(date.getTime() + ACCESS_TOKEN_EXPIRE_TIME).getTime();
    }

    public long getRefreshTokenExpirationMillis() {
        Date date = new Date();
        return new Date(date.getTime() + REFRESH_TOKEN_EXPIRE_TIME).getTime();
    }
}
