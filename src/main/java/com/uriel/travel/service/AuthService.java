package com.uriel.travel.service;

import com.uriel.travel.domain.Users;
import com.uriel.travel.dto.UserRequestDto;
import com.uriel.travel.dto.UserResponseDto;
import com.uriel.travel.exception.CustomBadRequestException;
import com.uriel.travel.exception.CustomException;
import com.uriel.travel.exception.CustomUnauthorizedException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.jwt.TokenProvider;
import com.uriel.travel.jwt.entity.RefreshToken;
import com.uriel.travel.jwt.entity.TokenResponseDto;
import com.uriel.travel.repository.RefreshTokenRepository;
import com.uriel.travel.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UsersRepository usersRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

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

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),loginDto.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        Long userId = tokenProvider.getMemberIdFromRefreshToken(refreshToken);
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUserId(userId)
                .orElse(RefreshToken.builder().user(Users.builder().id(userId).build())
                        .build());
        refreshTokenEntity.updateRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        return tokenProvider.createTokenDto(accessToken,refreshToken);
    }
    public void logout(HttpServletRequest request){
        String token= tokenProvider.getRefreshToken(request);
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(tokenProvider.getMemberIdFromRefreshToken(token))
                .orElseThrow(()->
                        new CustomUnauthorizedException(ErrorCode.REFRESH_TOKEN_NOT_EXIST));
        refreshTokenRepository.delete(refreshToken);
    }

    /**
     * 회원정보 관련
     */
//    public UserResponseDto.Profile updateUserProfile(UserRequestDto.Profile userRequestDto){
//
//    }


}
