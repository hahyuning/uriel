package com.uriel.travel.jwt;

import com.uriel.travel.exception.CustomException;
import com.uriel.travel.exception.CustomUnauthorizedException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.jwt.entity.RefreshToken;
import com.uriel.travel.jwt.entity.TokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class TokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    public static final String TOKEN_TYPE ="Bearer";
    @Value("${jwt.live.atk}")
    private long accessTokenValidTime;
    @Value("${jwt.live.rtk}")
    private long refreshTokenValidTime;
    RefreshToken refreshToken;

    @PostConstruct
    protected void init(){
      secretKey= Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    public String generateToken(Authentication authentication,long tokenValid) {
        Claims claims=Jwts.claims().setSubject(authentication.getName());
        Date date=new Date();
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setIssuer("uriel")
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenValid))
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }
    //access token 생성
    public String createAccessToken(Authentication authentication) {
       return this.generateToken(authentication,accessTokenValidTime);
    }
    public String createRefreshToken(Authentication authentication){
        return this.generateToken(authentication,refreshTokenValidTime);
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
        if(claims.get("auth") == null){
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }
        //클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
    public String getAccessToken(HttpServletRequest request){ //요청에서 토큰을 빼냄
        String token= Optional.ofNullable(request.getHeader("Authorization")).orElseThrow(()->
                new CustomException(ErrorCode.LOGIN_REQUIRED));
        if (!StringUtils.hasText(token) || !StringUtils.startsWithIgnoreCase(token, TOKEN_TYPE)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return token.substring(7);
    }
    public String getRefreshToken(HttpServletRequest request){
        String token = Optional.ofNullable(request.getHeader("refreshToken")).orElseThrow(()->
            new CustomException(ErrorCode.REFRESH_TOKEN_NOT_EXIST));
        if(StringUtils.hasText(token)){ //토큰 값이 비었으면,
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NULL);
        }
        return token.substring(7);
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e){
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }
    }

    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
    public String getUserEmail(String accessToken){
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody().getSubject();
    }
    /**
     * 리프레쉬 토큰 관련
     */
//    public TokenResponseDto updateRefreshToken(HttpServletRequest request){
//        String accessToken=getAccessToken(request);
//        String refreshToken=getRefreshToken(request);
//        if(!validateToken())
//    }
    public Long getMemberIdFromRefreshToken(String refreshToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        // 주체(Subject)를 추출하고 Long으로 변환하여 반환
        String subject = claims.getSubject();
        return Long.parseLong(subject);
    }




}