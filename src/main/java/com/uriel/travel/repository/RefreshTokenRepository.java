package com.uriel.travel.repository;

import com.uriel.travel.domain.Users;
import com.uriel.travel.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByUserId(Long userId);
    Optional<RefreshToken> findByUserEmail(String email);
}
