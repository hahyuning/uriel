package com.uriel.travel.security;

import com.uriel.travel.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*
사용자가 보낸 요청 헤더에 올바른 토큰이 왔다면, UserName과 authority가 포함된
authentication 객체를 생성해서 SecurityContext에 등록
*/

public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    public JwtFilter(TokenProvider tokenProvider){
        this.tokenProvider=tokenProvider;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = tokenProvider.getAccessToken(request);
        if(tokenProvider.validateToken(token)){
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }

}
