package com.dietmall.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.user.dto.UserProfileResponse;
import com.dietmall.user.dto.UserProfileUpdateRequest;
import com.dietmall.user.service.ProfileService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profile")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(
            ProfileService profileService) {

        this.profileService = profileService;
    }

    // 내 프로필 조회
    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        UserProfileResponse response =
                profileService.getProfile(userId);

        return ResponseEntity.ok(response);
    }

    // 내 프로필 수정
    @PatchMapping
    public ResponseEntity<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid UserProfileUpdateRequest request) {

        Long userId = Long.valueOf(jwt.getSubject());

        UserProfileResponse response =
                profileService.updateProfile(
                        userId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}