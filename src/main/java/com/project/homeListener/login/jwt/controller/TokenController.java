package com.project.homeListener.login.jwt.controller;

import com.project.homeListener.login.jwt.dto.request.ReIssueToken;
import com.project.homeListener.login.jwt.dto.response.JWTsResponse;
import com.project.homeListener.login.jwt.entity.UserInformInToken;
import com.project.homeListener.login.jwt.service.TokenService;
import com.project.homeListener.login.jwt.util.JWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/reIssue")
    public ResponseEntity<JWTsResponse> reIssueTokens(@RequestBody @Valid ReIssueToken reIssueToken) {
        return ResponseEntity.ok(tokenService.reIssueTokens(reIssueToken.getRefreshToken()));
    }
}
