package com.dietmall.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.stereotype.Component;

@Component
public class GoogleTokenVerifier {

    private static final String GOOGLE_ISSUER =
            "https://accounts.google.com";

    private final JwtDecoder jwtDecoder;


    public GoogleTokenVerifier(
            @Value("${google.client-id}") String clientId) {

        JwtDecoder decoder =
                JwtDecoders.fromIssuerLocation(GOOGLE_ISSUER);


        OAuth2TokenValidator<Jwt> issuerValidator =
                JwtValidators.createDefaultWithIssuer(
                        GOOGLE_ISSUER
                );


        OAuth2TokenValidator<Jwt> audienceValidator =
                jwt -> {

                    List<String> audience =
                            jwt.getAudience();

                    if (audience != null
                            && audience.contains(clientId)) {

                        return OAuth2TokenValidatorResult.success();
                    }


                    OAuth2Error error =
                            new OAuth2Error(
                                    "invalid_token",
                                    "구글 ID 토큰의 대상이 DietMall이 아닙니다.",
                                    null
                            );


                    return OAuth2TokenValidatorResult.failure(error);
                };


        if (decoder instanceof
                org.springframework.security.oauth2.jwt.NimbusJwtDecoder
                nimbusJwtDecoder) {

            nimbusJwtDecoder.setJwtValidator(
                    new DelegatingOAuth2TokenValidator<>(
                            issuerValidator,
                            audienceValidator
                    )
            );
        }


        this.jwtDecoder = decoder;
    }


    public Jwt verify(String idToken) {

        return jwtDecoder.decode(idToken);
    }
}