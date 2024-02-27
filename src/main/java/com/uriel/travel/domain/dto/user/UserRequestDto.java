package com.uriel.travel.domain.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

public class UserRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Login {
        String email;
        String password;
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SignUp {
        String userName;
        String enFirstName;
        String enLastName;
        LocalDate birth;
        String gender;

        String email; //회원 아이디
        String password;

        String phoneNumber;

        int headCount; //가족 인원수
        String childName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class EmailCheck {
        String email;
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {
        String userName;
        String enFirstName;
        String enLastName;
        LocalDate birth;
        String gender;

        String email; //회원 아이디

        String phoneNumber;
        int headCount; //가족 인원수
        String childName;
    }

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class FindEmail {
        String userName;
        String phoneNumber;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class NewPassword {
        String password;
    }
}
