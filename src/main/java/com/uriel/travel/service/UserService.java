package com.uriel.travel.service;

import com.uriel.travel.domain.dto.user.UserResponseDto;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto.UserInfo findByEmail(String email) {
        return UserResponseDto.UserInfo.of(userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER)));
    }
}
