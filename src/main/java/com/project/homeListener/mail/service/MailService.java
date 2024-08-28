package com.project.homeListener.mail.service;

import com.project.homeListener.login.jwt.entity.UserInformInToken;
import com.project.homeListener.login.user.service.UserService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final UserService userService;

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;


    private String generateEmailContent(String fileURL) {
        Context context = new Context();
        context.setVariable("fileURL", fileURL);

        return templateEngine.process("emailTemplate", context);
    }


    public void sendRecordingFiles(UserInformInToken userInform, String fileURL) {
        String userEmail = userService.findEmailByUserId(userInform.getId());

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");



            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject("Home Listener 이상상황 알림입니다.");
            mimeMessageHelper.setText(generateEmailContent(fileURL), true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to send email");
        }
    }
}
