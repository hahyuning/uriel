package com.uriel.travel.repository;

import com.uriel.travel.domain.SocialType;
import com.uriel.travel.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email=:email and u.krName=:userName")
    Optional<User> findByEmailAndUserName(String email, String userName);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
