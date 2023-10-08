package com.uriel.travel.dto;

import com.uriel.travel.domain.Gender;
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
        Gender gender;
        LocalDate birth;
        String email;
        String password;
        String phoneNumber;
        int headCount;
        String childName;
        public Users toUserEntity(){
            return Users.builder()
                    .userName(userName)
                    .gender(gender)
                    .birth(birth)
                    .email(email)
                    .password(password)
                    .phoneNumber(phoneNumber)
                    .headCount(headCount)
                    .childName(childName)
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
    public static class FindId {
        String userName;
        String phoneNumber;
    }
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class FindPw {
        String userName;
        String email;
        String password;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Profile {
        String email;
        String password;
        String userName;
        Gender gender;
        LocalDate birth;
        String phoneNumber;
        int headCount;
        String childName;
    }
//    @Getter
//    @Setter
//    @FieldDefaults(level = AccessLevel.PRIVATE)
//    public static class ChangePassword {
//        String password;
//    }

}
