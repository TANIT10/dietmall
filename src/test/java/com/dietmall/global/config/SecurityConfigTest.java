package com.dietmall.global.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

class SecurityConfigTest {

    @Test
    void 허용된_프론트주소만_CORS에_포함된다() {

        SecurityConfig securityConfig = new SecurityConfig();

        CorsConfigurationSource source =
                securityConfig.corsConfigurationSource();

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        CorsConfiguration configuration =
                source.getCorsConfiguration(request);

        assertNotNull(configuration);

        assertTrue(
                configuration.getAllowedOrigins()
                        .contains("http://localhost:5173")
        );

        assertTrue(
                configuration.getAllowedOrigins()
                        .contains("http://127.0.0.1:5173")
        );

        assertFalse(
                configuration.getAllowedOrigins()
                        .contains("https://evil.example.com")
        );
    }
}