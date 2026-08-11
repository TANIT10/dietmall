package com.dietmall.group.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.group.dto.DietLogNoteRequest;
import com.dietmall.group.dto.DietLogNoteResponse;
import com.dietmall.group.service.DietLogNoteService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping(
        "/api/groups/{groupId}/logs/{logId}/notes"
)
@SecurityRequirement(name = "bearerAuth")
public class DietLogNoteController {

    private final DietLogNoteService dietLogNoteService;


    public DietLogNoteController(
            DietLogNoteService dietLogNoteService) {

        this.dietLogNoteService =
                dietLogNoteService;
    }


    @PostMapping
    public ResponseEntity<DietLogNoteResponse> createNote(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @PathVariable Long logId,
            @Valid @RequestBody DietLogNoteRequest request) {

        Long userId =
                Long.valueOf(
                        jwt.getSubject()
                );


        DietLogNoteResponse response =
                dietLogNoteService.createNote(
                        userId,
                        groupId,
                        logId,
                        request
                );


        return ResponseEntity.ok(
                response
        );
    }


    @GetMapping
    public ResponseEntity<List<DietLogNoteResponse>> getNotes(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @PathVariable Long logId) {

        Long userId =
                Long.valueOf(
                        jwt.getSubject()
                );


        List<DietLogNoteResponse> response =
                dietLogNoteService.getNotes(
                        userId,
                        groupId,
                        logId
                );


        return ResponseEntity.ok(
                response
        );
    }


    @PutMapping("/{noteId}")
    public ResponseEntity<DietLogNoteResponse> updateNote(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @PathVariable Long logId,
            @PathVariable Long noteId,
            @Valid @RequestBody DietLogNoteRequest request) {

        Long userId =
                Long.valueOf(
                        jwt.getSubject()
                );


        DietLogNoteResponse response =
                dietLogNoteService.updateNote(
                        userId,
                        groupId,
                        logId,
                        noteId,
                        request
                );


        return ResponseEntity.ok(
                response
        );
    }


    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @PathVariable Long logId,
            @PathVariable Long noteId) {

        Long userId =
                Long.valueOf(
                        jwt.getSubject()
                );


        dietLogNoteService.deleteNote(
                userId,
                groupId,
                logId,
                noteId
        );


        return ResponseEntity
                .noContent()
                .build();
    }
}