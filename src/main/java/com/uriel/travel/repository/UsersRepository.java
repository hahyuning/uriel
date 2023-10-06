package com.uriel.travel.repository;

import com.uriel.travel.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    Optional<Users> findIdByUserName(String userName);

    Optional<Users> findByPhoneNumber(String phoneNumber);
    Optional<Users> findByEmail(String email);


}
