package com.uriel.travel.util;

import com.uriel.travel.exception.CustomUnauthorizedException;
import com.uriel.travel.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName() == null) {
            throw new CustomUnauthorizedException(ErrorCode.NOT_FOUND_MEMBER);
        }
        return authentication.getName();
    }
}
