package com.uriel.travel.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ROLE_ADMIN("ADMIN"), ROLE_USER("USER");

    private final String viewName;

    @JsonCreator
    public static Role from(String sub) {
        for (Role authority : Role.values()) {
            if (authority.getViewName().equals(sub)) {
                return authority;
            }
        }
        return null;
    }
}
