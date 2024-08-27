package com.project.homeListener.mail.controller;

import com.project.homeListener.login.jwt.entity.UserInformInToken;
import com.project.homeListener.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    @PostMapping
    public ResponseEntity<Void> sendMail(@AuthenticationPrincipal UserInformInToken userInform,
                                              @RequestParam("recordingFile") MultipartFile recordingFile) {
        mailService.sendRecordingFile(userInform, recordingFile);
        return ResponseEntity.ok().build();
    }

}
