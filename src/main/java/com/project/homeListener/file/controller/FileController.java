package com.project.homeListener.file.controller;

import com.project.homeListener.file.service.FileService;
import com.project.homeListener.login.jwt.entity.UserInformInToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.net.URL;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/presigned-url")
    public ResponseEntity<URL> getPresignedUrl(@AuthenticationPrincipal UserInformInToken userInform) throws URISyntaxException {
        URL url = fileService.getPresignedUrl(userInform);

        return ResponseEntity.ok(url);
    }

}
