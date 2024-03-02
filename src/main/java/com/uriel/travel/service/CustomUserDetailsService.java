package com.uriel.travel.service;

import com.uriel.travel.domain.Gender;
import com.uriel.travel.domain.Role;
import com.uriel.travel.domain.SocialType;
import com.uriel.travel.domain.dto.user.TokenDto;
import com.uriel.travel.domain.dto.user.UserRequestDto;
import com.uriel.travel.domain.entity.User;
import com.uriel.travel.jwt.JwtIssuer;
import com.uriel.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtIssuer jwtIssuer;

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

    public TokenDto login(UserRequestDto.Login requestDto) {
        User user = getMemberByEmail(requestDto.getEmail());

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("일치하는 정보가 없습니다.");
        }

        return jwtIssuer.createToken(user.getEmail(), user.getRole().getViewName());
    }
}