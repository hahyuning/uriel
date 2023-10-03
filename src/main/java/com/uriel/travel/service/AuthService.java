package com.uriel.travel.service;

import com.uriel.travel.domain.Users;
import com.uriel.travel.dto.UserRequestDto;
import com.uriel.travel.dto.UserResponseDto;
import com.uriel.travel.exception.CustomException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 정보 저장(회원가입)
     * @param  - 회원 정보
     * @return 회원가입 정보 + pk
     */
    @Transactional
    public UserResponseDto.SignUp SignUp(UserRequestDto.SignUp userRequestDto){
        checkDuplicateEmailNumber(userRequestDto.getEmail()); //1.이메일 체크
        checkDuplicatePhoneNumber(userRequestDto.getPhoneNumber()); //2. 번호 체크
        Users user=userRequestDto.toUserEntity();
        user.encodePassword(passwordEncoder);
        return UserResponseDto.SignUp.of(usersRepository.save(user));
    }
    //전화번호 중복 체크
    @Transactional(readOnly = true)
    public void checkDuplicatePhoneNumber(String phoneNumber) {
        usersRepository.findByPhoneNumber(phoneNumber)
                .ifPresent(member -> {
                    throw new CustomException(ErrorCode.DUPLICATE_PHONE_NUMBER);
                });
    }
    //이메일 중복 체크
    @Transactional(readOnly = true)
    public void checkDuplicateEmailNumber(String email) {
        usersRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
                });
    }
    /**
     * 로그인
     * @param  - 이메일(아이디),비밀번호
     * @return 회원가입 정보 + pk
     */

}
