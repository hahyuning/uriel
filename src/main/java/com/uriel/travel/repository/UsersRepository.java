package com.uriel.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Member;

@Repository
public interface UsersRepository extends JpaRepository<Member,Long> {

}
