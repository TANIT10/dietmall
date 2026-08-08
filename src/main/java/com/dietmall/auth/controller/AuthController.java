package com.dietmall.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.auth.dto.LoginRequest;
import com.dietmall.auth.dto.LoginResponse;
import com.dietmall.auth.dto.RefreshTokenRequest;
import com.dietmall.auth.dto.SignupRequest;
import com.dietmall.auth.dto.TokenRefreshResponse;
import com.dietmall.auth.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 일반 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(
            @RequestBody @Valid SignupRequest request) {

        Long userId = authService.signup(request);

        return ResponseEntity.ok(
                Map.of(
                        "message", "회원가입 성공",
                        "userId", userId
                )
        );
    }

    // 일반 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request) {

        LoginResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(
            @RequestBody @Valid RefreshTokenRequest request) {

        TokenRefreshResponse response =
                authService.refresh(request);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
    		@RequestBody @Valid RefreshTokenRequest request) {
    	
    	authService.logout(request);
    	
    	return ResponseEntity.ok(
    			Map.of(
    					"message", "로그아웃 성공"
    					)
    			);
    }
}