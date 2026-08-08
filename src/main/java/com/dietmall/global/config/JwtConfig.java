package com.dietmall.global.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {

    private final String jwtSecret;

    public JwtConfig(
            @Value("${jwt.secret}") String jwtSecret) {

        this.jwtSecret = jwtSecret;
    }

    private SecretKey secretKey() {

        byte[] keyBytes =
                Base64.getDecoder().decode(jwtSecret);

        return new SecretKeySpec(
                keyBytes,
                "HmacSHA256"
        );
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        return NimbusJwtDecoder
                .withSecretKey(secretKey())
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {

        return NimbusJwtEncoder
                .withSecretKey(secretKey())
                .algorithm(MacAlgorithm.HS256)
                .build();
    }
}