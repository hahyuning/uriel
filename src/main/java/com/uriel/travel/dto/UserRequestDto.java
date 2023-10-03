package com.uriel.travel.dto;

import com.uriel.travel.domain.Users;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

public class UserRequestDto {
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SignUp{
        String userName;
        LocalDate birth;
        String email;
        String password;
        String phoneNumber;
        int headCount;
        String childName;
        public Users toUserEntity(){
            return Users.builder()
                    .userName(userName)
                    .birth(birth)
                    .email(email)
                    .password(password)
                    .phoneNumber(phoneNumber)
                    .headCount(headCount)
                    .build();
        }
    }
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class login{
        String email;
        String password;

    }
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PhoneNumber {
        String phoneNumber;
    }
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Email {
        String email;
    }
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class verifyEmail {
        String email;
    }

}
