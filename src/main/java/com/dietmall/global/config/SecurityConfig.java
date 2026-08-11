package com.dietmall.global.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Value("${DEV_ENDPOINTS_ENABLED:true}")
    private boolean devEndpointsEnabled;


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
                .cors(Customizer.withDefaults())

                .csrf(csrf ->
                        csrf.disable()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // 로그인 없이 사용할 수 있는 인증 API
                        .requestMatchers(
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/auth/refresh",
                                "/api/auth/logout",
                                "/api/auth/kakao/login",
                                "/api/auth/google/login",
                                "/login/oauth2/code/kakao"
                        ).permitAll()

                        // 서버 상태 확인
                        .requestMatchers(
                                "/api/health"
                        ).permitAll()

                        // WebSocket 연결 입구
                        // 실제 JWT 인증은 STOMP CONNECT에서 검사
                        .requestMatchers(
                                "/ws",
                                "/ws/**"
                        ).permitAll()

                        // 개발용 테스트 페이지
                        // DEV_ENDPOINTS_ENABLED=false 이면 차단
                        .requestMatchers(
                                "/google-test.html",
                                "/websocket-test.html"
                        )
                        .access((_, _) ->
                                new AuthorizationDecision(
                                        devEndpointsEnabled
                                )
                        )

                        // Swagger
                        // DEV_ENDPOINTS_ENABLED=false 이면 차단
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        )
                        .access((_, _) ->
                                new AuthorizationDecision(
                                        devEndpointsEnabled
                                )
                        )

                        // 그 외 API는 JWT 필요
                        .anyRequest()
                        .authenticated()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(
                                Customizer.withDefaults()
                        )
                );

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();


        // 개발용 프론트 주소
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "http://127.0.0.1:5173"
                )
        );


        // 허용 HTTP 메서드
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );


        // 프론트에서 보낼 수 있는 Header
        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type",
                        "Accept"
                )
        );


        // 프론트에서 확인할 수 있는 Header
        configuration.setExposedHeaders(
                List.of(
                        "Authorization"
                )
        );


        configuration.setAllowCredentials(
                true
        );


        configuration.setMaxAge(
                3600L
        );


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration(
                "/**",
                configuration
        );


        return source;
    }
}