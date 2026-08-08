package com.dietmall.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.auth.entity.RefreshToken;
import com.dietmall.auth.exception.InvalidRefreshTokenException;
import com.dietmall.auth.repository.RefreshTokenRepository;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.UserRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final long refreshTokenExpiration;

    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            @Value("${jwt.refresh-token-expiration}")
            long refreshTokenExpiration) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // 새로운 Refresh Token 생성
    @Transactional
    public String createRefreshToken(Long userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        )
                );

        String rawToken = generateRandomToken();

        String tokenHash = hashToken(rawToken);

        Instant expiresAt =
                Instant.now()
                        .plusMillis(refreshTokenExpiration);

        RefreshToken refreshToken = new RefreshToken(
                user,
                tokenHash,
                expiresAt
        );

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    // Refresh Token 검사 + 한 번 사용 후 폐기
    @Transactional
    public Long verifyAndConsume(String rawToken) {

        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(InvalidRefreshTokenException::new);

        if (!refreshToken.getExpiresAt().isAfter(Instant.now())) {
            throw new InvalidRefreshTokenException();
        }

        Long userId = refreshToken.getUser().getId();

        refreshTokenRepository.delete(refreshToken);

        return userId;
    }

    // 로그아웃: Refresh Token 폐기
    @Transactional
    public void revoke(String rawToken) {

        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(InvalidRefreshTokenException::new);

        refreshTokenRepository.delete(refreshToken);
    }

    private String generateRandomToken() {

        byte[] bytes = new byte[32];

        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String hashToken(String rawToken) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hashBytes =
                    digest.digest(
                            rawToken.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            return HexFormat.of()
                    .formatHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "SHA-256을 사용할 수 없습니다.",
                    e
            );
        }
    }
}