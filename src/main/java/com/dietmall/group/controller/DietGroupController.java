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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dietmall.group.dto.GroupCreateRequest;
import com.dietmall.group.dto.GroupJoinRequest;
import com.dietmall.group.dto.GroupListItemResponse;
import com.dietmall.group.dto.GroupProfileImageResponse;
import com.dietmall.group.dto.GroupProfileResponse;
import com.dietmall.group.dto.GroupProfileUpdateRequest;
import com.dietmall.group.dto.GroupResponse;
import com.dietmall.group.entity.GroupMember;
import com.dietmall.group.repository.GroupMemberRepository;
import com.dietmall.group.service.DietGroupService;
import com.dietmall.group.service.GroupProfileImageService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/groups")
@SecurityRequirement(name = "bearerAuth")
public class DietGroupController {

    private final DietGroupService dietGroupService;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupProfileImageService groupProfileImageService;


    public DietGroupController(
            DietGroupService dietGroupService,
            GroupMemberRepository groupMemberRepository,
            GroupProfileImageService groupProfileImageService) {

        this.dietGroupService =
                dietGroupService;

        this.groupMemberRepository =
                groupMemberRepository;

        this.groupProfileImageService =
                groupProfileImageService;
    }


    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody GroupCreateRequest request) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        GroupResponse response =
                dietGroupService.createGroup(
                        userId,
                        request
                );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/{groupId}/join")
    public ResponseEntity<GroupResponse> joinPublicGroup(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @Valid @RequestBody GroupJoinRequest request) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        GroupResponse response =
                dietGroupService.joinPublicGroup(
                        userId,
                        groupId,
                        request
                );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/join-by-code/{inviteCode}")
    public ResponseEntity<GroupResponse> joinPrivateGroup(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String inviteCode,
            @Valid @RequestBody GroupJoinRequest request) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        GroupResponse response =
                dietGroupService.joinPrivateGroup(
                        userId,
                        inviteCode,
                        request
                );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/public")
    public ResponseEntity<List<GroupListItemResponse>> getPublicGroups() {

        List<GroupListItemResponse> response =
                dietGroupService.getPublicGroups();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/my")
    public ResponseEntity<List<GroupListItemResponse>> getMyGroups(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        List<GroupListItemResponse> response =
                dietGroupService.getMyGroups(
                        userId
                );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{groupId}/profile")
    public ResponseEntity<GroupProfileResponse> getMyGroupProfile(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        GroupProfileResponse response =
                dietGroupService.getMyGroupProfile(
                        userId,
                        groupId
                );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{groupId}/profile")
    public ResponseEntity<GroupProfileResponse> updateMyGroupProfile(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @Valid @RequestBody GroupProfileUpdateRequest request) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        GroupProfileResponse response =
                dietGroupService.updateMyGroupProfile(
                        userId,
                        groupId,
                        request
                );

        return ResponseEntity.ok(response);
    }


    @PostMapping(
            value = "/{groupId}/profile/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<GroupProfileImageResponse> uploadMyProfileImage(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @RequestPart("file") MultipartFile file) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        GroupProfileImageResponse response =
                dietGroupService.uploadMyProfileImage(
                        userId,
                        groupId,
                        file
                );

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{groupId}/profile/image")
    public ResponseEntity<GroupProfileImageResponse> deleteMyProfileImage(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        GroupProfileImageResponse response =
                dietGroupService.deleteMyProfileImage(
                        userId,
                        groupId
                );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{groupId}/members/{memberId}/profile/image")
    public ResponseEntity<Resource> getMemberProfileImage(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long groupId,
            @PathVariable Long memberId)
            throws IOException {

        Long userId =
                Long.valueOf(jwt.getSubject());


        boolean joined =
                groupMemberRepository
                        .existsByGroupIdAndUserId(
                                groupId,
                                userId
                        );


        if (!joined) {
            throw new IllegalArgumentException(
                    "가입한 그룹이 아닙니다."
            );
        }


        GroupMember member =
                groupMemberRepository
                        .findById(memberId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "그룹 멤버를 찾을 수 없습니다."
                                )
                        );


        if (!member.getGroup()
                .getId()
                .equals(groupId)) {

            throw new IllegalArgumentException(
                    "해당 그룹의 멤버가 아닙니다."
            );
        }


        String savedFileName =
                member.getProfileImageUrl();


        if (savedFileName == null
                || savedFileName.isBlank()) {

            throw new IllegalArgumentException(
                    "프로필 사진이 없습니다."
            );
        }


        Path filePath =
                groupProfileImageService
                        .getFilePath(
                                savedFileName
                        );


        if (!Files.exists(filePath)) {

            throw new IllegalArgumentException(
                    "프로필 사진 파일을 찾을 수 없습니다."
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