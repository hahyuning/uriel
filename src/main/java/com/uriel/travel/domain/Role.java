package com.uriel.travel.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ROLE_ADMIN, ROLE_USER, ROLE_GUEST;

    @JsonCreator
    public static Role from(String sub) {
        for (Role authority : Role.values()) {
            if (authority.toString().equals(sub)) {
                return authority;
            }
        }
        return null;
    }
}
