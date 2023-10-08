package com.uriel.travel.dto;

import com.uriel.travel.domain.Gender;
import com.uriel.travel.domain.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

public class UserResponseDto {
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SignUp{
        Long id;
        String userName;
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

}
