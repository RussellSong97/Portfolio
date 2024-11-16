package com.cuv.domain.email;

import com.cuv.common.JSONResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final String sendUserName = "cartongryung1@gmail.com";
    private final String password = "mqkh obsi htbs coww";

    public JSONResponse<?> sendEmail(String email, String userName, String returnUrl) throws JsonProcessingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendUserName, password);
            }
        });

        InputStream stream = getClass().getClassLoader().getResourceAsStream("templates/main/mail_form.html");

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
        } catch (IOException e) {
            log.error("이메일 전송 오류: {}", e.getMessage());
            throw new RuntimeException("이메일 전송 오류", e);
        }

        String emailContent = contentBuilder.toString();
        emailContent = emailContent.replace("{originName}", userName);
        emailContent = emailContent.replace("{email}", email);
        emailContent = emailContent.replace("{returnUrl}", returnUrl);
        try {
            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.toString());
            helper.setFrom(new InternetAddress("cartongryung1@gmail.com"));
            helper.setTo(email);
            helper.setSubject("카통령 비밀번호 재설정 메일");
            helper.setText(emailContent, true);
            helper.addInline("logo_mail", new ClassPathResource("static/user/images/common/img/mail_logo.png"));

            Transport.send(message);
        } catch (MessagingException e) {
            log.error("이메일 전송 오류: {}", e.getMessage());
            throw new RuntimeException("이메일 전송 오류", e);
        }
        return new JSONResponse<>(200, "인증 메일을 발송하였습니다. 메일함을 확인해주세요");
    }
}
