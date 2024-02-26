package com.uriel.travel.domain.dto.user;

import com.uriel.travel.domain.Gender;
import com.uriel.travel.domain.entity.Users;
import com.uriel.travel.domain.dto.socialLogin.SocialLoginResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserResponseDto {
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SignUp{
        Long id;
        String userName;
        String enFirstName;
        String enLastName;
        LocalDate birth;
        String email;
        String password;
        String phoneNumber;
        int headCount;
        String childName;
        Gender gender;
        public static SignUp of(Users users){
            return SignUp.builder()
                    .id(users.getId())
                    .userName(users.getUserName())
                    .enFirstName(users.getEnFirstName())
                    .enLastName(users.getEnLastName())
                    .birth(users.getBirth())
                    .email(users.getEmail())
                    .password(users.getPassword())
                    .phoneNumber(users.getPhoneNumber())
                    .headCount(users.getHeadCount())
                    .childName(users.getChildName())
                    .gender(users.getGender())
                    .build();
        }
    }
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Profile{
        String email;
        String password;
        String userName;
        Gender gender;
        LocalDate birth;
        String phoneNumber;
        int headCount;
        String childName;

        public static Profile of(Users users){
            return Profile.builder()
                    .email(users.getEmail())
                    .password(users.getPassword())
                    .userName(users.getUserName())
                    .gender(users.getGender())
                    .birth(users.getBirth())
                    .phoneNumber(users.getPhoneNumber())
                    .headCount(users.getHeadCount())
                    .childName(users.getChildName())
                    .build();
        }
    }
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Simple{
        Long id;
        public static Simple of(Users users){
            return Simple.builder()
                    .id(users.getId())
                    .build();
        }
    }
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class FindId{
        String email;
        public static FindId of(String email){
            return FindId.builder()
                    .email(email)
                    .build();
        }
    }

    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class FindPw{
        String password;

        public static FindPw of(Users user){
            return FindPw.builder()
                    .password(user.getPassword())
                    .build();
        }
    }
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class NaverUser {
        Long id;
        String email;
        String name;
        Gender gender;
        LocalDate birthday;
        String password;
        String phoneNumber;
        public static NaverUser of(SocialLoginResponseDto.NaverDto.Response response){
            LocalDate birthday = getBirth(response.getBirthyear(),response.getBirthday());
            Gender parseGender = Gender.mapToGender(response.getGender());
            return NaverUser.builder()
                    .id(Long.parseLong(response.getId()))
                    .email(response.getEmail())
                    .name(response.getName())
                    .gender(parseGender)
                    .birthday(birthday)
                    .phoneNumber(response.getMobile())
                    .build();
        }

        public static LocalDate getBirth(String birthday, String birthyear) {
            StringBuilder stringBuilder = new StringBuilder().append(birthyear).append("-").append(birthday);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(stringBuilder, formatter);
        }
    }
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class KakaoUser {
        String id;
        String email;
        String name;
        Gender gender;
        LocalDate birthday;
        String password;
        String phoneNumber;
        public static NaverUser of(SocialLoginResponseDto.KakaoDto response){
            LocalDate birthday = getBirth(response.getKakao_account().getBirthyear(),response.getKakao_account().getBirthday());
            Gender parseGender = Gender.mapToGender(response.getKakao_account().getGender());
            return NaverUser.builder()
                    .id(response.getId())
                    .email(response.getKakao_account().getEmail())
                    .name(response.getKakao_account().getName())
                    .gender(parseGender)
                    .birthday(birthday)
                    .phoneNumber(response.getKakao_account().getPhone_number())
                    .build();
        }

        public static LocalDate getBirth(String birthday, String birthyear) {
            StringBuilder stringBuilder = new StringBuilder().append(birthyear).append("-").append(birthday);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(stringBuilder, formatter);
        }
    }




}
