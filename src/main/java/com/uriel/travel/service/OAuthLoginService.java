package com.uriel.travel.service;

import com.uriel.travel.domain.Gender;
import com.uriel.travel.domain.SocialType;
import com.uriel.travel.domain.dto.user.UserRequestDto;
import com.uriel.travel.domain.dto.user.UserResponseDto;
import com.uriel.travel.domain.entity.User;
import com.uriel.travel.jwt.JwtToken;
import com.uriel.travel.jwt.JwtTokenProvider;
import com.uriel.travel.oauth2.OAuthInfoResponse;
import com.uriel.travel.oauth2.OAuthLoginParams;
import com.uriel.travel.oauth2.RequestOAuthInfoService;
import com.uriel.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthLoginService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RequestOAuthInfoService requestOAuthInfoService;

//    public JwtToken login(OAuthLoginParams params) {
//        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
//        String email = findOrCreateMember(oAuthInfoResponse);
//        return jwtTokenProvider.createToken(email, "ROLE_USER");
//    }

//    private String findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
//        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
//                .map(User::getEmail)
//                .orElseGet(() -> newMember(oAuthInfoResponse));
//    }

//    private String newMember(OAuthInfoResponse oAuthInfoResponse) {
//        User user = User.builder()
//                .email(oAuthInfoResponse.getEmail())
//                .krName(oAuthInfoResponse.getName())
//                .gender(Gender.mapToGender(oAuthInfoResponse.getGender()))
//                .phoneNumber(oAuthInfoResponse.getPhoneNumber())
//                .socialType(oAuthInfoResponse.getOAuthProvider())
//                .build();
//
//        return userRepository.save(user).getEmail();
//    }

    public UserResponseDto.SocialUserInfo login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        return findUser(oAuthInfoResponse);
    }

    private UserResponseDto.SocialUserInfo findUser(OAuthInfoResponse oAuthInfoResponse) {
        Optional<User> findUser = userRepository.findByEmail(oAuthInfoResponse.getEmail());
        if (findUser.isPresent()) {
            JwtToken jwtToken = jwtTokenProvider.createToken(findUser.get().getEmail(), "ROLE_USER");
            return UserResponseDto.SocialUserInfo
                    .builder()
                    .accessToken(jwtToken.getAccessToken())
                    .refreshToken(jwtToken.getRefreshToken()).build();
        } else {
            return UserResponseDto.SocialUserInfo.builder()
                    .email(oAuthInfoResponse.getEmail())
                    .userName(oAuthInfoResponse.getName())
                    .gender(Gender.mapToGender(oAuthInfoResponse.getGender()).getViewName())
                    .phoneNumber(oAuthInfoResponse.getPhoneNumber())
                    .birth(oAuthInfoResponse.getBirth()).build();
        }
    }

    public JwtToken updateUserInfo(UserRequestDto.Update requestDto) {
        User user = User.builder()
                .email(requestDto.getEmail())
                .krName(requestDto.getUserName())
                .enFirstName(requestDto.getEnFirstName())
                .enLastName(requestDto.getEnLastName())
                .birth(requestDto.getBirth())
                .gender(Gender.from(requestDto.getGender()))
                .phoneNumber(requestDto.getPhoneNumber())
                .socialType(SocialType.from(requestDto.getSocialType()))
                .headCount(requestDto.getHeadCount())
                .childName(requestDto.getChildName())
                .build();

        String email = userRepository.save(user).getEmail();
        return jwtTokenProvider.createToken(email, "ROLE_USER");
    }
}
