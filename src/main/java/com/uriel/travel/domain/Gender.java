package com.uriel.travel.domain;

public enum Gender {
    Male,Female,Other;

    public static Gender mapToGender(String genderString) {
        if ("M".equalsIgnoreCase(genderString)) {
            return Gender.Male;
        } else if ("F".equalsIgnoreCase(genderString)) {
            return Gender.Female;
        } else {
            return Gender.Other;
        }
    }

}
