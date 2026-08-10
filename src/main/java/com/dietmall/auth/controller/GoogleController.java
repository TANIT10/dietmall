package com.dietmall.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.auth.dto.GoogleLoginRequest;
import com.dietmall.auth.dto.LoginResponse;
import com.dietmall.auth.service.GoogleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleController {

    private final GoogleService googleService;

    public GoogleController(GoogleService googleService) {
        this.googleService = googleService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody GoogleLoginRequest request) {

        LoginResponse response =
                googleService.login(request.getIdToken());

        return ResponseEntity.ok(response);
    }
}