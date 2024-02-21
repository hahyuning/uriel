package com.uriel.travel.service.Login;

import com.uriel.travel.domain.Authority;
import com.uriel.travel.domain.SocialType;
import com.uriel.travel.domain.Users;
import com.uriel.travel.dto.SocialLogin.SocialTokenResponseDto;
import com.uriel.travel.dto.UserRequestDto;
import com.uriel.travel.dto.UserResponseDto;
import com.uriel.travel.exception.*;
import com.uriel.travel.jwt.TokenProvider;
import com.uriel.travel.jwt.domain.TokenResponseDto;
import com.uriel.travel.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UsersRepository usersRepository;
//    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final List<SocialLoginService> loginServices;

//    private final JwtTokenProvider jwtTokenProvider;
//    private final AES128Config aes128Config;
//    private final RedisService redisService;

    /**
     * 회원 정보 저장(회원가입)
     * @param  - 회원 정보
     * @return 회원가입 정보 + pk
     */
    public UserResponseDto.SignUp signUp(UserRequestDto.SignUp userRequestDto){
        checkDuplicateEmailNumber(userRequestDto.getEmail()); //1.이메일 체크
        checkDuplicatePhoneNumber(userRequestDto.getPhoneNumber()); //2. 번호 체크
        Users user = userRequestDto.toUserEntity();
//        user.encodePassword(passwordEncoder);
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
//        user.setEncodePassword(userRequestDto.getPassword(),passwordEncoder);
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
//        resetPassword(user,userRequestDto.getPassword());
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
//    @Transactional
//    public void resetPassword(Users user,String password){
//        user.setEncodePassword(password,passwordEncoder);
//    }


    // 로그아웃
    public void logout(String encryptedRefreshToken, String accessToken){
//        this.verifiedRefreshToken(encryptedRefreshToken);
//        String refreshToken = aes128Config.decryptAes(encryptedRefreshToken);
//        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
//        String email = claims.getSubject();
//        String redisRefreshToken = redisService.getValues(email);
//
//        if (redisRefreshToken.checkExistsValue(redisRefreshToken)) {
//            redisService.deleteValues(email);
//            redisService.setValues(accessToken, "logout", Duration.ofMillis(JwtTokenProvider.ACCESS_TOKEN_EXPIRE_TIME))
//        }
    }

    private void verifiedRefreshToken(String encryptedRefreshToken) {
        if (encryptedRefreshToken == null) {
            throw new CustomUnauthorizedException(ErrorCode.REFRESH_TOKEN_NOT_EXIST);
        }
    }

    private Users findUserByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
    }
}
