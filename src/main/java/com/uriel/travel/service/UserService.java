package com.uriel.travel.service;

import com.uriel.travel.domain.Users;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.RefreshTokenRepository;
import com.uriel.travel.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UsersRepository usersRepository;

    /* 현재 로그인 중인 사용자의 pk 값 받아오기 */
    public Long getLoginMemberId() {
        Users user = usersRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER)
        );
        return user.getId();
    }

}
