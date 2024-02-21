package com.uriel.travel.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Authority {
    AUTH_ADMIN("ADMIN"), AUTH_USER("USER");
    private final String viewName;

    @JsonCreator
    public static Authority from(String sub) {
        for (Authority authority : Authority.values()) {
            if (authority.getViewName().equals(sub)) {
                return authority;
            }
        }
        return null;
    }
}
