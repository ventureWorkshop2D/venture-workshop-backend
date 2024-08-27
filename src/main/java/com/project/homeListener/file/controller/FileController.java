package com.project.homeListener.file.controller;

import com.project.homeListener.file.service.S3FileUploader;
import com.project.homeListener.login.jwt.entity.UserInformInToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final S3FileUploader s3FileUploader;

    @PostMapping("/presigned-url")
    public ResponseEntity<URL> getPresignedUrl(@AuthenticationPrincipal UserInformInToken userInform) {
        URL url = s3FileUploader.generatePresignedUrl(userInform.getId());

        return ResponseEntity.ok(url);
    }
}
