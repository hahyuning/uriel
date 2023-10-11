package com.uriel.travel.service.Login;

import com.uriel.travel.domain.Authority;
import com.uriel.travel.domain.Gender;
import com.uriel.travel.domain.SocialType;
import com.uriel.travel.domain.Users;
import com.uriel.travel.dto.SocialLogin.SocialTokenResponseDto;
import com.uriel.travel.dto.UserRequestDto;
import com.uriel.travel.dto.UserResponseDto;
import com.uriel.travel.exception.*;
import com.uriel.travel.jwt.TokenProvider;
import com.uriel.travel.jwt.entity.RefreshToken;
import com.uriel.travel.jwt.entity.TokenResponseDto;
import com.uriel.travel.repository.RefreshTokenRepository;
import com.uriel.travel.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements UserDetailsService {
    private final UsersRepository usersRepository;
//    private final VerifiedUserRepository verifiedUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final List<SocialLoginService> loginServices;
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
                .username(users.getId().toString())
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

        /* 사용자 인증 완료 후 토큰 발급 + 이메일로 Claims subject를 설정 */
        String accessToken = tokenProvider.createAccessToken(authentication,user.getAuthority());
        String refreshToken = tokenProvider.createRefreshToken(authentication,user.getAuthority());

        /* 발급한 리프레쉬 토큰을 따로 저장, 레포에 없으면 새로 빌드 */
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUserId(user.getId())
                .orElse(RefreshToken.builder().user(user).build());
        refreshTokenEntity.updateRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
        user.setRefreshToken(refreshTokenEntity);

        return tokenProvider.createTokenDto(accessToken,refreshToken);
    }
    /**
     * 소셜 로그인
     */
    private SocialLoginService getSocialService(SocialType socialType){
        for (SocialLoginService loginService: loginServices) {
            if(socialType.equals(loginService.getSocialType())){
                return loginService;
            }
        }
        throw new CustomNotFoundException(ErrorCode.BAD_REQUEST);
    }
    public TokenResponseDto doSocialLogin(String code, SocialType socialType){
       SocialLoginService socialLoginService = getSocialService(socialType);
       SocialTokenResponseDto socialTokenResponseDto = socialLoginService.getAccessToken(code);
       UserResponseDto.NaverUser userResponseDto = socialLoginService.getUserInfo(socialTokenResponseDto.getAccess_token());

       if(usersRepository.findById((userResponseDto.getId())).isEmpty()){
           Users user = usersRepository.save(
                   Users.builder()
                           .id(userResponseDto.getId())
                           .email(userResponseDto.getEmail())
                           .gender(userResponseDto.getGender())
                           .authority(Authority.AUTH_USER)
                           .birth(userResponseDto.getBirthday())
                           .phoneNumber(userResponseDto.getPhoneNumber())
                           .build()
           );
       }
       Users user =usersRepository.findById(userResponseDto.getId()).orElseThrow(()->
               new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));

        String accessToken = socialTokenResponseDto.getAccess_token();
        String refreshToken = socialTokenResponseDto.getRefresh_token();

        return tokenProvider.createTokenDto(accessToken,refreshToken);
    }
    /** 로그아웃 로직
    1. request: 사용자가 로그아웃을 요청합니다.
    2. 요청에 들어온 refresh token을 가져옵니다.
    3. 요청의 token refreshToken 저장소의 token 과 비교합니다.
    4. 맞으면 토큰을 지웁니다.
     */
    //todo: 수정 필요,,
//    public void logout(HttpServletRequest request){
//        String token= tokenProvider.getRefreshToken(request); //refresh token
//        if(!tokenProvider.validateToken(token)){
//            throw new CustomBadRequestException(ErrorCode.BAD_REQUEST);
//        }
//        Users user = usersRepository.findByEmail(tokenProvider.getAuthentication(token).getName()).orElseThrow(()->
//                new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
//        if(userService.getLoginMemberId().equals(user.getId())){
//            RefreshToken refreshToken = refreshTokenRepository.findByUserId(userService.getLoginMemberId()) //이 토큰이 누구 것인가
//                    .orElseThrow(()->
//                            new CustomUnauthorizedException(ErrorCode.REFRESH_TOKEN_NOT_EXIST));
//            refreshTokenRepository.delete(refreshToken);
//        }
//    }
    public Long kakaoLogout(String accessToken){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization","Bearer "+ accessToken);
        RestTemplate rt = new RestTemplate();
        String requestUrl = "https://kapi.kakao.com/v1/user/logout";
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        //RestTemplate 초기화
        ResponseEntity<String> response = rt.exchange(
                requestUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        return Long.parseLong(Objects.requireNonNull(response.getBody()));
    }

    /**
     * 회원정보 관련
     */
    @Transactional
    public void updateUserProfile(UserRequestDto.Profile userRequestDto){
        Users user = usersRepository.findById(userService.getLoginMemberId()).orElseThrow(()->
                new CustomUnauthorizedException(ErrorCode.LOGIN_REQUIRED));

        user.setEmail(userRequestDto.getEmail());
        user.setEncodePassword(userRequestDto.getPassword(),passwordEncoder);
        user.setUserName(userRequestDto.getUserName());
        user.setGender(userRequestDto.getGender());
        user.setBirth(userRequestDto.getBirth());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setHeadCount(userRequestDto.getHeadCount());
        user.setChildName(userRequestDto.getChildName());

    }
    public UserResponseDto.Profile getUserProfile(){
        Users user = usersRepository.findById(userService.getLoginMemberId()).orElseThrow(()->
                new CustomUnauthorizedException(ErrorCode.LOGIN_REQUIRED));
        return UserResponseDto.Profile.of(user);
    }
    public UserResponseDto.FindId findId(UserRequestDto.FindId userRequestDto){
        Users loginUser = usersRepository.findById(userService.getLoginMemberId()).orElseThrow(()->
                new CustomUnauthorizedException(ErrorCode.LOGIN_REQUIRED));
        Users user = usersRepository.findIdByUserNameAndPhoneNumber(userRequestDto.getUserName(),userRequestDto.getPhoneNumber()).orElseThrow(()->
                new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        if(loginUser.getId().equals(user.getId())){
            return UserResponseDto.FindId.of(user.getEmail());
        }
        throw new CustomForbiddenException(ErrorCode.FORBIDDEN);
    }
    public void findPw(UserRequestDto.FindPw userRequestDto){
        Users user = usersRepository.findIdByUserNameAndEmail(userRequestDto.getUserName(),userRequestDto.getEmail()).orElseThrow(()->
                new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        resetPassword(user,userRequestDto.getPassword());
    }
//    public void isValidate(UserRequestDto.FindPw userRequestDto){
//        Optional<VerifiedUser> verifiedUserOptional = verifiedUserRepository.findIdByUserNameAndEmail(userRequestDto.getUserName(), userRequestDto.getEmail());
//        if(verifiedUserOptional.isPresent()){
//            VerifiedUser verifiedUser = verifiedUserOptional.get();
//            if(!verifiedUser.getEmail().equals(userRequestDto.getEmail()) || !verifiedUser.getUserName().equals(userRequestDto.getUserName())){
//                throw new CustomUnauthorizedException(ErrorCode.FORBIDDEN);
//            }
//
//        }
//    }
    @Transactional
    public void resetPassword(Users user,String password){
        user.setEncodePassword(password,passwordEncoder);
    }


}
