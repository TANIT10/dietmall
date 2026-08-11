package com.dietmall.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.user.dto.NotificationSettingResponse;
import com.dietmall.user.dto.NotificationSettingUpdateRequest;
import com.dietmall.user.service.NotificationSettingService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/notifications/settings")
@SecurityRequirement(name = "bearerAuth")
public class NotificationSettingController {

    private final NotificationSettingService notificationSettingService;


    public NotificationSettingController(
            NotificationSettingService notificationSettingService) {

        this.notificationSettingService =
                notificationSettingService;
    }


    @GetMapping
    public ResponseEntity<NotificationSettingResponse> getSetting(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        NotificationSettingResponse response =
                notificationSettingService.getSetting(userId);

        return ResponseEntity.ok(response);
    }


    @PutMapping
    public ResponseEntity<NotificationSettingResponse> updateSetting(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody NotificationSettingUpdateRequest request) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        NotificationSettingResponse response =
                notificationSettingService.updateSetting(
                        userId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}