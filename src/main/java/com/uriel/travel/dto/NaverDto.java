package com.uriel.travel.dto;

import lombok.Builder;

import java.time.LocalDate;
@Builder

public class NaverDto {
    private String id;
    private String userName;
    private String email;
    private String gender;
    private String birthday;
    private String birthYear;
    private String phoneNumber;

}
