package com.uriel.travel.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Authority {
    AUTH_ADMIN("admin"), AUTH_USER("user");
    private final String authority;

}
