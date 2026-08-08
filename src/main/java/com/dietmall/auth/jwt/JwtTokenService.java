package com.dietmall.auth.jwt;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final long accessTokenExpiration;

    public JwtTokenService(
            JwtEncoder jwtEncoder,
            @Value("${jwt.access-token-expiration}")
            long accessTokenExpiration) {

        this.jwtEncoder = jwtEncoder;
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String createAccessToken(Long userId) {

        Instant now = Instant.now();

        Instant expiresAt =
                now.plusMillis(accessTokenExpiration);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiresAt(expiresAt)
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .type("JWT")
                .build();

        return jwtEncoder
                .encode(
                        JwtEncoderParameters.from(
                                header,
                                claims
                        )
                )
                .getTokenValue();
    }
}