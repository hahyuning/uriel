package com.uriel.travel.service;

import com.uriel.travel.domain.dto.MailDto;
import com.uriel.travel.domain.entity.User;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MailService {

    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String mailSenderAddress;

    public MailDto sendMailAndChangePw(MailDto requestDto) {
        User user = userRepository.findByEmailAndUserName(requestDto.getEmail(), requestDto.getUserName())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));

        // 임시 비밀번호 발급
        String tempPw = getTempPassword();

        // 비밀번호 업데이트
        user.updatePassword(passwordEncoder.encode(tempPw));

        // 메일 전송
        MailDto mailDto = MailDto.builder()
                .email(requestDto.getEmail())
                .title("아이디/비밀번호 정보입니다.")
                .message("요청하신 계정 정보는 아래와 같습니다.\n" +
                        "\n" +
                        "아이디: " + requestDto.getEmail() + "\n" +
                        "\n" +
                        "임시 비밀번호: " + tempPw + " 입니다." + "\n" +
                        "\n" +
                        "아래 링크를 클릭하시면 위에 적힌 비밀번호로 바뀌게 됩니다. 로그인 하신 후 비밀번호를 바꾸어 주세요.")
                .build();

        mailSend(mailDto);
        return mailDto;
    }

    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    public void mailSend(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getEmail());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        message.setFrom(mailSenderAddress);
        message.setReplyTo(mailSenderAddress);
        mailSender.send(message);
    }
}
