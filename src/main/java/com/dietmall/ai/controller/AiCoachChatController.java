package com.dietmall.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.ai.dto.CoachChatRequest;
import com.dietmall.ai.dto.CoachChatResponse;
import com.dietmall.ai.service.AiCoachChatService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ai/coach")
@SecurityRequirement(name = "bearerAuth")
public class AiCoachChatController {

    private final AiCoachChatService
            aiCoachChatService;

    public AiCoachChatController(
            AiCoachChatService aiCoachChatService
    ) {
        this.aiCoachChatService =
                aiCoachChatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<CoachChatResponse>
            createChatResponse(
                    @AuthenticationPrincipal Jwt jwt,

                    @RequestBody
                    @Valid
                    CoachChatRequest request
            ) {
        Long userId =
                Long.valueOf(jwt.getSubject());

        CoachChatResponse response =
                aiCoachChatService
                        .createChatResponse(
                                userId,
                                request
                        );

        return ResponseEntity.ok(response);
    }
}