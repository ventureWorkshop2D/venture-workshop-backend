package com.project.homeListener.mail.service;

import com.project.homeListener.login.jwt.entity.UserInformInToken;
import com.project.homeListener.login.user.service.UserService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final UserService userService;

    private final JavaMailSender javaMailSender;

    public void sendRecordingFiles(UserInformInToken userInform, String fileURL) {
        String userEmail = userService.findEmailByUserId(userInform.getId());

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject("Home Listener alert");
            mimeMessageHelper.setText("Something might be happening at home. Please check the audio file link below.\n" + fileURL);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to send email");
        }
    }
}
