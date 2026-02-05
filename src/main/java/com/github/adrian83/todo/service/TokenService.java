package com.github.adrian83.todo.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.adrian83.todo.domain.RefreshToken;
import com.github.adrian83.todo.domain.User;
import com.github.adrian83.todo.repository.RefreshTokenRepository;
import com.github.adrian83.todo.service.exception.TokenValidationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_USERNAME = "username";

    private final String jwtSecret;
    private final long accessTokenExpireMinutes;
    private final long refreshTokenExpireDays;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(
            @Value("${app.jwt.secret:MyVerySecureSecretKeyThatShouldBeAtLeast32CharactersLongForHS256}") String jwtSecret,
            @Value("${app.jwt.access-token-expire-minutes:15}") long accessTokenExpireMinutes,
            @Value("${app.jwt.refresh-token-expire-days:7}") long refreshTokenExpireDays,
            RefreshTokenRepository refreshTokenRepository) {
        this.jwtSecret = jwtSecret;
        this.accessTokenExpireMinutes = accessTokenExpireMinutes;
        this.refreshTokenExpireDays = refreshTokenExpireDays;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(accessTokenExpireMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(CLAIM_USER_ID, user.getId())
                .claim(CLAIM_USERNAME, user.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public RefreshToken generateRefreshToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(refreshTokenExpireDays, ChronoUnit.DAYS);
        
        RefreshToken refreshToken = new RefreshToken(user, tokenValue, expiresAt);
        return refreshTokenRepository.save(refreshToken);
    }

    public Claims validateAccessToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new TokenValidationException("Invalid access token", e);
        }
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenValidationException("Refresh token not found"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenValidationException("Refresh token has expired");
        }

        return refreshToken;
    }

    public void revokeRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshTokenRepository::delete);
    }

    public void revokeAllRefreshTokens(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public Long extractUserId(Claims claims) {
        return claims.get(CLAIM_USER_ID, Long.class);
    }

    public String extractUsername(Claims claims) {
        return claims.get(CLAIM_USERNAME, String.class);
    }
}
