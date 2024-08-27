package com.project.homeListener.login.jwt.util;

import com.project.homeListener.login.jwt.entity.UserInformInToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
public class JWTUtil {

    private final String idClaimKey = "id";

    private final SecretKey accessTokenSecretKey;
    private final Long accessTokenExpiration;
    private final String accessTokenHeaderName;

    private final SecretKey refreshTokenSecretKey;
    private final Long refreshTokenExpiration;
    private final String refreshTokenHeaderName;


    private final String tokenPrefix;




    public JWTUtil(@Value("${jwt.accessTokenSecretKey}") String accessTokenSecretKey,
                   @Value("${jwt.accessTokenExpiration}") Long accessTokenExpiration,
                   @Value("${jwt.accessTokenHeaderName}") String accessTokenHeaderName,
                   @Value("${jwt.refreshTokenSecretKey}") String refreshTokenSecretKey,
                   @Value("${jwt.refreshTokenExpiration}") Long refreshTokenExpiration,
                   @Value("${jwt.refreshTokenHeaderName}") String refreshTokenHeaderName,
                   @Value("${jwt.tokenPrefix}") String tokenPrefix) {

        byte[] accessTokenKeyBytes = Decoders.BASE64.decode(accessTokenSecretKey);
        this.accessTokenSecretKey = Keys.hmacShaKeyFor(accessTokenKeyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.accessTokenHeaderName = accessTokenHeaderName;

        byte[] refreshTokenKeyBytes = Decoders.BASE64.decode(refreshTokenSecretKey);
        this.refreshTokenSecretKey = Keys.hmacShaKeyFor(refreshTokenKeyBytes);
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.refreshTokenHeaderName = refreshTokenHeaderName;

        this.tokenPrefix = tokenPrefix;
    }

    public String generateAccessToken(Long id, String subject) {
        return Jwts.builder()
                .subject(subject)
                .claim(idClaimKey, id)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(accessTokenSecretKey)
                .compact();
    }

    public String generateRefreshToken(Long id, String subject) {
        return Jwts.builder()
                .subject(subject)
                .claim(idClaimKey, id)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(refreshTokenSecretKey)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parser()
                    .verifyWith(accessTokenSecretKey)
                    .build()
                    .parseSignedClaims(accessToken);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parser()
                    .verifyWith(refreshTokenSecretKey)
                    .build()
                    .parseSignedClaims(refreshToken);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Optional<String> getAccessTokenByRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(accessTokenHeaderName);
        Optional<String> optionalAccessToken = Optional.ofNullable(accessToken);
        if (optionalAccessToken.isEmpty()) return Optional.empty();

        return Optional.of(accessToken.replace(tokenPrefix, ""));
    }

    public UserInformInToken getUserInformInAccessToken(String accessToken) {
        Claims claims = Jwts.parser()
                .verifyWith(accessTokenSecretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

        Long id = claims.get(idClaimKey, Long.class);
        String subject = claims.getSubject();

        return new UserInformInToken(id, subject);
    }

    public UserInformInToken getUserInformInRefreshToken(String accessToken) {
        Claims claims = Jwts.parser()
                .verifyWith(refreshTokenSecretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

        Long id = claims.get(idClaimKey, Long.class);
        String subject = claims.getSubject();

        return new UserInformInToken(id, subject);
    }

    public Long getRefreshTokenRemainTime(String refreshToken) {
        return Jwts.parser()
                .verifyWith(refreshTokenSecretKey)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload().getExpiration().getTime() - new Date().getTime();
    }

}
