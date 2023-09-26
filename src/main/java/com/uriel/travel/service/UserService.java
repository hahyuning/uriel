package com.uriel.travel.service;

import com.uriel.travel.jwt.TokenProvider;
import com.uriel.travel.jwt.dto.TokenResponseDto;
import com.uriel.travel.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    public TokenResponseDto login(String userId,String password){
        //1. id,pw 로 토큰을 생성한다.
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userId,password);
        //2. 사용자 비밀번호 체크
        Authentication authentication=authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //3. 인증 정보를 기반으로 jwt 토큰을 생성
        return tokenProvider.generateToken(authentication);
    }

}
