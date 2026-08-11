package com.dietmall.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.notification.dto.PushDeviceRegisterRequest;
import com.dietmall.notification.service.PushDeviceService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications/devices")
@SecurityRequirement(name = "bearerAuth")
public class PushDeviceController {

    private final PushDeviceService pushDeviceService;


    public PushDeviceController(
            PushDeviceService pushDeviceService) {

        this.pushDeviceService = pushDeviceService;
    }


    @PostMapping
    public ResponseEntity<Void> registerDevice(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PushDeviceRegisterRequest request) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        pushDeviceService.register(
                userId,
                request
        );

        return ResponseEntity.ok().build();
    }


    @DeleteMapping
    public ResponseEntity<Void> unregisterDevice(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String fid) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        pushDeviceService.unregister(
                userId,
                fid
        );

        return ResponseEntity.ok().build();
    }
}