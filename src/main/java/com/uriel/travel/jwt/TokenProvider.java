package com.uriel.travel.jwt;

import com.uriel.travel.domain.Authority;
import com.uriel.travel.exception.CustomUnauthorizedException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.jwt.entity.TokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Slf4j
@Component
public class TokenProvider {
    private final Key key;
    private static final String AUTHORITIES_KEY = "auth";
    private static final String TOKEN_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;              // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일
    public TokenProvider( @Value("${jwt.secret}") String secretKey){
        key= Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(Authentication authentication,long tokenValid,Authority authority) {
        Date date=new Date();
        return Jwts.builder()
                .setIssuer("uriel")
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY,authority)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenValid))
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }
    //access token 생성
    public String createAccessToken(Authentication authentication,Authority authority) {
       return this.generateToken(authentication,ACCESS_TOKEN_EXPIRE_TIME,authority);
    }
    public String createRefreshToken(Authentication authentication, Authority authority){
        return this.generateToken(authentication,REFRESH_TOKEN_EXPIRE_TIME,authority);
    }
    public TokenResponseDto createTokenDto(String at, String rt){
        return TokenResponseDto.builder()
                .accessToken(at)
                .refreshToken(rt)
                .grantType(TOKEN_TYPE)
                .build();
    }
    //jwt 토큰 안에 있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaims(accessToken);
        if(claims.get(AUTHORITIES_KEY) == null){
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }
        //클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
    public boolean validateToken(String token){
       try{
           Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
           return true;
       }catch (CustomUnauthorizedException customUnauthorizedException){
           throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
       }
    }
    public String getAccessToken(HttpServletRequest request) {
        String token = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElseThrow(() ->
                new CustomUnauthorizedException(ErrorCode.LOGIN_REQUIRED));

        if (!StringUtils.hasText(token) || !StringUtils.startsWithIgnoreCase(token, TOKEN_TYPE)) {
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        return token.substring(7);
    }
    public String getRefreshToken(HttpServletRequest request) {
        String token = getCookieByName(request).orElseThrow(() ->
                new CustomUnauthorizedException(ErrorCode.REFRESH_TOKEN_NOT_EXIST)
        );

        if (!StringUtils.hasText(token) || !StringUtils.startsWithIgnoreCase(token, TOKEN_TYPE)) {
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        return token.substring(7);
    }
    private Optional<String> getCookieByName(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Refresh-Token")) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }
    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }


}