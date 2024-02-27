package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.user.UserRequestDto;
import com.uriel.travel.domain.dto.user.UserResponseDto;
import com.uriel.travel.service.UserService;
import com.uriel.travel.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 이메일 중복 확인
    @PostMapping("/email")
    private BaseResponse<Void> emailCheck(@RequestBody UserRequestDto.EmailCheck emailDto) {
        userService.emailCheck(emailDto.getEmail());
        return BaseResponse.ok();
    }

    // 마이 페이지 - 회원정보 조회
    @GetMapping("/mypage")
    private BaseResponse<UserResponseDto.UserInfo> getUserInfo() {
        String loginUserEmail = SecurityUtil.getCurrentUsername();
        return BaseResponse.ok(userService.getUserInfo(loginUserEmail));
    }

    // 자녀 이름 조회
    @GetMapping("/child")
    private BaseResponse<UserResponseDto.Child> emailCheck() {
        return BaseResponse.ok(userService.getChildName(SecurityUtil.getCurrentUsername()));
    }

    // 회원 정보 수정
    @PutMapping
    private BaseResponse<UserResponseDto.UserInfo> updateUserInfo(@RequestBody UserRequestDto.Update userRequestDto) {
        return BaseResponse.ok(userService.updateUserInfo(userRequestDto));
    }

    // 아이디 찾기
    @PostMapping("/find-id")
    private BaseResponse<UserResponseDto.EmailInfo> findEmail(@RequestBody UserRequestDto.FindEmail userRequestDto) {
        return BaseResponse.ok(userService.findEmail(userRequestDto));
    }

    // 비밀번호 재설정 링크 메일 전송
//    @PostMapping("/send-mail")
//    private BaseResponse<Void> sendEmail(@RequestBody MailDto mailDto) {
//
//    }
}