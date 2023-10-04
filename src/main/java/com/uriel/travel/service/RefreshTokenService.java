package com.uriel.travel.service;

import com.uriel.travel.domain.Users;
import com.uriel.travel.jwt.entity.RefreshToken;
import com.uriel.travel.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    //리프레쉬 토큰을 재발급 하는 서비스


}
