package com.uriel.travel.service;

import com.uriel.travel.domain.Users;
import com.uriel.travel.dto.UserRequestDto;
import com.uriel.travel.dto.UserResponseDto;
import com.uriel.travel.exception.CustomBadRequestException;
import com.uriel.travel.exception.CustomException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.jwt.TokenProvider;
import com.uriel.travel.jwt.entity.TokenResponseDto;
import com.uriel.travel.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    /**
     * 회원 정보 저장(회원가입)
     * @param  - 회원 정보
     * @return 회원가입 정보 + pk
     */
    @Transactional
    public UserResponseDto.SignUp signUp(UserRequestDto.SignUp userRequestDto){
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
                    throw new CustomBadRequestException(ErrorCode.DUPLICATE_PHONE_NUMBER);
                });
    }
    //이메일 중복 체크
    @Transactional(readOnly = true)
    public void checkDuplicateEmailNumber(String email) {
        usersRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new CustomBadRequestException(ErrorCode.DUPLICATE_EMAIL);
                });
    }
    /**
     * 로그인
     * @param  - 이메일(아이디),비밀번호
     * @return 회원가입 정보 + pk
     */
    public TokenResponseDto login(UserRequestDto.login loginDto){
        Users user=usersRepository.findByEmail(loginDto.getEmail()).orElseThrow(()->
                new CustomBadRequestException(ErrorCode.LOGIN_EMAIL_ERROR));
        boolean matches=passwordEncoder.matches(loginDto.getPassword(),user.getPassword());
        if(!matches) throw new CustomBadRequestException(ErrorCode.LOGIN_PASSWORD_ERROR);
        return tokenProvider.createTokenDto(loginDto.getEmail());
    }
    public void logout(HttpServletRequest request){
        String refreshToken= tokenProvider.getRefreshToken(request);

    }
    /**
     * 회원정보 수정
     * @param  -
     * @return
     */


}
