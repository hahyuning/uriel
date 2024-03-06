package com.uriel.travel.repository;

import com.uriel.travel.domain.SocialType;
import com.uriel.travel.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
