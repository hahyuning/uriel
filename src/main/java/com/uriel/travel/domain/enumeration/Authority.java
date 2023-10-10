package com.uriel.travel.domain.enumeration;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Authority {
    AUTH_ADMIN("admin"), AUTH_USER("user");
    private final String authority;

}
