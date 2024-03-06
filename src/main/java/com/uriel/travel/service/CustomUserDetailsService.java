package com.uriel.travel.service;

import com.uriel.travel.domain.Gender;
import com.uriel.travel.domain.Role;
import com.uriel.travel.domain.SocialType;
import com.uriel.travel.domain.dto.user.JwtToken;
import com.uriel.travel.domain.dto.user.UserRequestDto;
import com.uriel.travel.domain.entity.User;
import com.uriel.travel.exception.CustomUnauthorizedException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.jwt.JwtTokenProvider;
import com.uriel.travel.repository.UserRepository;
import com.uriel.travel.util.Aes256Util;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RedisService redisService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getMemberByEmail(email);
    }

    private User getMemberByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("일치하는 정보가 없습니다."));
    }

    public User signUp(UserRequestDto.SignUp requestDto) {
        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new RuntimeException("사용중인 이메일입니다.");
        }
        return userRepository.save(User.builder()
                        .krName(requestDto.getUserName())
                        .enFirstName(requestDto.getEnFirstName())
                        .enLastName(requestDto.getEnLastName())
                        .gender(Gender.from(requestDto.getGender()))
                        .birth(requestDto.getBirth())
                        .email(requestDto.getEmail())
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .phoneNumber(requestDto.getPhoneNumber())
                        .headCount(requestDto.getHeadCount())
                        .childName(requestDto.getChildName())
                        .role(Role.ROLE_USER)
                        .socialType(SocialType.LOCAL)
                        .build());
    }

//    public JwtToken socialSignIn(UserRequestDto.SignUp requestDto) {
//        User user;
//        try {
//            user = getMemberByEmail(requestDto.getEmail());
//        } catch (UsernameNotFoundException e) {
//            user = userRepository.save(User.builder()
//                    .krName(requestDto.getUserName())
//                    .enFirstName(requestDto.getEnFirstName())
//                    .enLastName(requestDto.getEnLastName())
//                    .gender(Gender.from(requestDto.getGender()))
//                    .birth(requestDto.getBirth())
//                    .email(requestDto.getEmail())
//                    .password(passwordEncoder.encode(requestDto.getPassword()))
//                    .phoneNumber(requestDto.getPhoneNumber())
//                    .headCount(requestDto.getHeadCount())
//                    .childName(requestDto.getChildName())
//                    .role(Role.ROLE_USER)
//                    .socialType(SocialType.KAKAO)
//                    .build());
//        }
//
//        return jwtTokenProvider.createToken(user.getEmail(), user.getRole().getViewName());
//    }

    public void logout(String encryptedRefreshToken, String accessToken) {
        verifiedRefreshToken(encryptedRefreshToken);

        String refreshToken = Aes256Util.decrypt(encryptedRefreshToken);
        Claims claims = jwtTokenProvider.getClaims(refreshToken);
        String email = Aes256Util.decrypt(claims.getSubject());

        log.info("이메일 " + email);
        String redisRefreshToken = redisService.getValues(email);
        log.info("레디스에 저장된 refreshToken" + redisRefreshToken);
        if (redisService.checkExistsValue(redisRefreshToken)) {
            redisService.deleteValues(email);

            // 로그아웃 시 Access Token Redis 저장 ( key = Access Token / value = "logout" )
            long accessTokenExpirationMillis = jwtTokenProvider.getAccessTokenExpirationMillis();
            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
        }
    }

    private void verifiedRefreshToken(String encryptedRefreshToken) {
        if (encryptedRefreshToken == null) {
            throw new CustomUnauthorizedException(ErrorCode.BAD_AUTHORIZATION_CODE);
        }
    }

    public String reissueAccessToken(String encryptedRefreshToken) {
        this.verifiedRefreshToken(encryptedRefreshToken);
        String refreshToken = Aes256Util.decrypt(encryptedRefreshToken);
        Claims claims = jwtTokenProvider.getClaims(refreshToken);
        String email = Aes256Util.decrypt(claims.getSubject());
        String redisRefreshToken = redisService.getValues(email);

        if (redisService.checkExistsValue(redisRefreshToken) && refreshToken.equals(redisRefreshToken)) {
            User user = this.getMemberByEmail(email);
            JwtToken tokenDto = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());
            String newAccessToken = tokenDto.getAccessToken();

            return newAccessToken;
        } else throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
    }
}