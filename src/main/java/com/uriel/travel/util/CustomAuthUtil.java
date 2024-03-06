package com.uriel.travel.util;

import com.uriel.travel.domain.Role;
import com.uriel.travel.exception.CustomUnauthorizedException;
import com.uriel.travel.exception.ErrorCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class CustomAuthUtil {

    public static List<GrantedAuthority> createAuthorities(String role) {
        return List.of(new SimpleGrantedAuthority(role));
    }

    public static void verifiedRole(String role) {
        if (role == null) {
            throw new CustomUnauthorizedException(ErrorCode.MEMBER_ROLE_INVALID);
        } else if (!role.equals(Role.ROLE_ADMIN.toString()) && !role.equals(Role.ROLE_USER.toString())) {
            throw new CustomUnauthorizedException(ErrorCode.MEMBER_ROLE_INVALID);
        }
    }
}
