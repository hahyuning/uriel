package com.uriel.travel.domain.dto.user;

import com.uriel.travel.domain.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

public class UserResponseDto {

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class LoginResult {
        String result;
        String account;
        String errorMessage;
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserInfo {
        String email;
        String userName;
        String enFirstName;
        String enLastName;
        String gender;
        LocalDate birth;
        String phoneNumber;
        int headCount;
        String childName;

        public static UserInfo of(User user) {
            return UserInfo.builder()
                    .email(user.getEmail())
                    .userName(user.getKrName())
                    .enFirstName(user.getEnFirstName())
                    .enLastName(user.getEnLastName())
                    .gender(user.getGender().getViewName())
                    .birth(user.getBirth())
                    .phoneNumber(user.getPhoneNumber())
                    .headCount(user.getHeadCount())
                    .childName(user.getChildName())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Child {
        String childName;
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class EmailInfo {
        String email;
    }
}
