package com.uriel.travel.service;

import com.uriel.travel.domain.Authority;
import com.uriel.travel.domain.Users;
import com.uriel.travel.dto.UserRequestDto;
import com.uriel.travel.dto.UserResponseDto;
import com.uriel.travel.exception.*;
import com.uriel.travel.jwt.TokenProvider;
import com.uriel.travel.jwt.entity.RefreshToken;
import com.uriel.travel.jwt.entity.TokenResponseDto;
import com.uriel.travel.repository.RefreshTokenRepository;
import com.uriel.travel.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final UserService userService;

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
        user.setAuthority(Authority.AUTH_USER); //회원가입을 진행하면 자동으로 유저가 됨
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
     * 로그인 관련
     */
    /*  AuthenticationProvider 가 회원정보를 인증할 수 있도록 시스템 상으로 회원 정보를 불러오는 메서드 */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = usersRepository.findByEmail(email).orElseThrow(()->
                new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        return User.builder()
                .username(users.getEmail())
                .password(users.getPassword())
                .build();
    }
    public TokenResponseDto login(UserRequestDto.login loginDto){
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),loginDto.getPassword()
        ); // id: email , pw 로 시큐리티에 인증 요청

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        /* 인증 정보를 가져오기 위함 */
        Users user=usersRepository.findByEmail(loginDto.getEmail()).orElseThrow(()->
                new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER)
        );

        /* 사용자 인증 완료 후 토큰 발급 +이메일로 Claims subject를 설정 */
        String accessToken = tokenProvider.createAccessToken(loginDto.getEmail(),user.getAuthority());
        String refreshToken = tokenProvider.createRefreshToken(loginDto.getEmail(),user.getAuthority());



        /* 발급한 리프레쉬 토큰을 따로 저장, 레포에 없으면 새로 빌드 */
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUserId(user.getId())
                .orElse(RefreshToken.builder().user(Users.builder().id(user.getId()).build())
                        .build());
        refreshTokenEntity.updateRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        return tokenProvider.createTokenDto(accessToken,refreshToken);
    }
    /** 로그아웃 로직
    1. request: 사용자가 로그아웃을 요청합니다.
    2. 요청에 들어온 refresh token을 가져옵니다.
    3. 요청의 token refreshToken 저장소의 token 과 비교합니다.
    4. 맞으면 토큰을 지웁니다.
     */
    public void logout(HttpServletRequest request){
        request.getHeader(REFRESH_TOKEN);
        String token= tokenProvider.getRefreshToken(request); //refresh token
        System.out.println(token);
        System.out.println(userService.getLoginMemberId());
        //todo: 현재 로그인한 사용자랑 로그아웃 요청한 사용자가 맞는지 확인하는 로직을 추가해야 할까..?
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userService.getLoginMemberId()) //이 토큰이 누구 것인가
                .orElseThrow(()->
                        new CustomUnauthorizedException(ErrorCode.REFRESH_TOKEN_NOT_EXIST));
        refreshTokenRepository.delete(refreshToken);
        System.out.println("로그아웃 완료했음");
    }


    /**
     * 회원정보 관련
     */
//    public UserResponseDto.Profile updateUserProfile(UserRequestDto.Profile userRequestDto){
//
//    }


}
