package com.uriel.travel.service;

import com.uriel.travel.domain.dto.user.UserRequestDto;
import com.uriel.travel.domain.dto.user.UserResponseDto;
import com.uriel.travel.domain.entity.User;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // 이메일 체크
    @Transactional(readOnly = true)
    public void emailCheck(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);

        if (userByEmail.isPresent()) {
            throw new CustomNotFoundException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    // 회원정보 조회
    @Transactional(readOnly = true)
    public UserResponseDto.UserInfo getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));

        return UserResponseDto.UserInfo.of(user);
    }

    // 자녀 이름 조회
    @Transactional(readOnly = true)
    public UserResponseDto.Child getChildName(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent() || user.get().getChildName() == null) {
            return UserResponseDto.Child
                    .builder()
                    .childName("")
                    .build();
        }

        return UserResponseDto.Child
                .builder()
                .childName(user.get().getChildName())
                .build();
    }

    // 회원정보 수정
    public UserResponseDto.UserInfo updateUserInfo(UserRequestDto.Update userRequestDto) {
        User user = userRepository.findByEmail(userRequestDto.getEmail())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));

        user.update(userRequestDto);

        return UserResponseDto.UserInfo.of(user);
    }

    // 이름 + 핸드폰 번호로 이메일 찾기
    public UserResponseDto.EmailInfo findEmail(UserRequestDto.FindEmail userRequestDto) {
        User user = userRepository.findByPhoneNumber(userRequestDto.getPhoneNumber())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));

        if (!user.getKrName().equals(userRequestDto.getUserName())) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER);
        }

        return UserResponseDto.EmailInfo
                .builder()
                .email(user.getEmail()).build();
    }

    public void updatePassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        user.updatePassword(password);
    }

    public void updateMarketingAgreement(String email, boolean agreement) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        user.setMarketing(agreement);
    }
}
