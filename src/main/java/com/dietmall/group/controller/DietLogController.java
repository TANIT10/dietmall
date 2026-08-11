package com.dietmall.group.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dietmall.group.dto.DietLogCreateRequest;
import com.dietmall.group.dto.DietLogResponse;
import com.dietmall.group.dto.DietLogUpdateRequest;
import com.dietmall.group.entity.DietLog;
import com.dietmall.group.repository.DietLogRepository;
import com.dietmall.group.service.DietLogImageService;
import com.dietmall.group.service.DietLogService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/groups/{groupId}/logs")
@SecurityRequirement(name = "bearerAuth")
public class DietLogController {

    private final DietLogService dietLogService;
    private final DietLogRepository dietLogRepository;
    private final DietLogImageService dietLogImageService;


    public DietLogController(
            DietLogService dietLogService,
            DietLogRepository dietLogRepository,
            DietLogImageService dietLogImageService) {

        this.dietLogService =
                dietLogService;

        this.dietLogRepository =
                dietLogRepository;

        this.dietLogImageService =
                dietLogImageService;
    }


    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DietLogResponse> createLog(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @RequestParam(
                    name = "memo",
                    required = false
            ) String memo,
            @RequestPart("file") MultipartFile file) {

        Long userId =
                Long.valueOf(jwt.getSubject());


        DietLogCreateRequest request =
                new DietLogCreateRequest();

        request.setMemo(
                memo
        );


        DietLogResponse response =
                dietLogService.createLog(
                        userId,
                        groupId,
                        request,
                        file
                );


        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<DietLogResponse>> getGroupLogs(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId) {

        Long userId =
                Long.valueOf(jwt.getSubject());


        List<DietLogResponse> response =
                dietLogService.getGroupLogs(
                        userId,
                        groupId
                );


        return ResponseEntity.ok(response);
    }


    @GetMapping("/{logId}")
    public ResponseEntity<DietLogResponse> getLog(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @PathVariable Long logId) {

        Long userId =
                Long.valueOf(jwt.getSubject());


        DietLogResponse response =
                dietLogService.getLog(
                        userId,
                        groupId,
                        logId
                );


        return ResponseEntity.ok(response);
    }


    @PutMapping("/{logId}/memo")
    public ResponseEntity<DietLogResponse> updateMemo(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @PathVariable Long logId,
            @Valid @RequestBody DietLogUpdateRequest request) {

        Long userId =
                Long.valueOf(jwt.getSubject());


        DietLogResponse response =
                dietLogService.updateMemo(
                        userId,
                        groupId,
                        logId,
                        request
                );


        return ResponseEntity.ok(response);
    }


    @PutMapping(
            value = "/{logId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DietLogResponse> updateImage(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @PathVariable Long logId,
            @RequestPart("file") MultipartFile file) {

        Long userId =
                Long.valueOf(jwt.getSubject());


        DietLogResponse response =
                dietLogService.updateImage(
                        userId,
                        groupId,
                        logId,
                        file
                );


        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteLog(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @PathVariable Long logId) {

        Long userId =
                Long.valueOf(jwt.getSubject());


        dietLogService.deleteLog(
                userId,
                groupId,
                logId
        );


        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{logId}/image")
    public ResponseEntity<Resource> getLogImage(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @PathVariable Long logId)
            throws IOException {

        Long userId =
                Long.valueOf(jwt.getSubject());


        dietLogService.getLog(
                userId,
                groupId,
                logId
        );


        DietLog log =
                dietLogRepository
                        .findByIdAndGroupId(
                                logId,
                                groupId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "다이어트 로그를 찾을 수 없습니다."
                                )
                        );


        Path filePath =
                dietLogImageService
                        .getFilePath(
                                log.getImageUrl()
                        );


        if (!Files.exists(filePath)) {

            throw new IllegalArgumentException(
                    "로그 사진 파일을 찾을 수 없습니다."
            );
        }


        Resource resource =
                new FileSystemResource(
                        filePath
                );


        String contentType =
                Files.probeContentType(
                        filePath
                );


        MediaType mediaType;

        if (contentType == null) {

            mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;

        } else {

            mediaType =
                    MediaType.parseMediaType(
                            contentType
                    );
        }


        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(resource);
    }
}