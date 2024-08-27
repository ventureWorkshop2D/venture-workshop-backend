package com.project.homeListener.login.jwt.service;

import com.project.homeListener.login.jwt.dto.response.JWTsResponse;
import com.project.homeListener.login.jwt.entity.UserInformInToken;
import com.project.homeListener.login.jwt.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JWTUtil jwtUtil;

    public JWTsResponse reIssueTokens(String refreshToken) {
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("invalid refresh token");
        }

        UserInformInToken userInform = jwtUtil.getUserInformInRefreshToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(userInform.getId(), userInform.getUserId());
        String newRefreshToken = jwtUtil.generateRefreshToken(userInform.getId(), userInform.getUserId());

        return new JWTsResponse(newAccessToken, newRefreshToken);
    }
}
