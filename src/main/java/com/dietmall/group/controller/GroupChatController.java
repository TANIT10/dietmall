package com.dietmall.group.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.group.dto.GroupChatMessageRequest;
import com.dietmall.group.dto.GroupChatMessageResponse;
import com.dietmall.group.dto.GroupChatPageResponse;
import com.dietmall.group.service.GroupChatService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/groups/{groupId}/chat")
@SecurityRequirement(name = "bearerAuth")
public class GroupChatController {

    private final GroupChatService groupChatService;


    public GroupChatController(
            GroupChatService groupChatService) {

        this.groupChatService =
                groupChatService;
    }


    @PostMapping("/messages")
    public ResponseEntity<GroupChatMessageResponse> sendMessage(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @Valid @RequestBody GroupChatMessageRequest request) {

        Long userId =
                Long.valueOf(
                        jwt.getSubject()
                );


        GroupChatMessageResponse response =
                groupChatService.sendMessage(
                        userId,
                        groupId,
                        request
                );


        return ResponseEntity.ok(
                response
        );
    }


    @GetMapping("/messages")
    public ResponseEntity<GroupChatPageResponse> getMessages(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @RequestParam(
                    defaultValue = "0"
            ) int page,
            @RequestParam(
                    defaultValue = "20"
            ) int size) {

        Long userId =
                Long.valueOf(
                        jwt.getSubject()
                );


        GroupChatPageResponse response =
                groupChatService.getMessages(
                        userId,
                        groupId,
                        page,
                        size
                );


        return ResponseEntity.ok(
                response
        );
    }
}