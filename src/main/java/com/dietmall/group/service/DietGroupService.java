package com.dietmall.group.service;

import java.security.SecureRandom;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import com.dietmall.group.dto.GroupCreateRequest;
import com.dietmall.group.dto.GroupJoinRequest;
import com.dietmall.group.dto.GroupListItemResponse;
import com.dietmall.group.dto.GroupProfileImageResponse;
import com.dietmall.group.dto.GroupProfileResponse;
import com.dietmall.group.dto.GroupProfileUpdateRequest;
import com.dietmall.group.dto.GroupResponse;
import com.dietmall.group.entity.DietGroup;
import com.dietmall.group.entity.GroupMember;
import com.dietmall.group.entity.GroupVisibility;
import com.dietmall.group.repository.DietGroupRepository;
import com.dietmall.group.repository.GroupMemberRepository;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.UserRepository;

@Service
public class DietGroupService {

    private static final Logger log =
            LoggerFactory.getLogger(
                    DietGroupService.class
            );


    private static final String INVITE_CODE_CHARACTERS =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private static final int INVITE_CODE_LENGTH = 6;


    private final SecureRandom secureRandom =
            new SecureRandom();


    private final DietGroupRepository dietGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final GroupProfileImageService groupProfileImageService;


    public DietGroupService(
            DietGroupRepository dietGroupRepository,
            GroupMemberRepository groupMemberRepository,
            UserRepository userRepository,
            GroupProfileImageService groupProfileImageService) {

        this.dietGroupRepository =
                dietGroupRepository;

        this.groupMemberRepository =
                groupMemberRepository;

        this.userRepository =
                userRepository;

        this.groupProfileImageService =
                groupProfileImageService;
    }


    @Transactional
    public GroupResponse createGroup(
            Long userId,
            GroupCreateRequest request) {

        User user =
                getUser(
                        userId
                );


        String groupNickname =
                normalizeGroupNickname(
                        request.getGroupNickname()
                );


        String inviteCode =
                generateUniqueInviteCode();


        DietGroup group =
                new DietGroup(
                        request.getName().trim(),
                        normalizeDescription(
                                request.getDescription()
                        ),
                        request.getVisibility(),
                        inviteCode,
                        user
                );


        DietGroup savedGroup =
                dietGroupRepository.save(
                        group
                );


        GroupMember ownerMember =
                new GroupMember(
                        savedGroup,
                        user,
                        groupNickname,
                        null
                );


        groupMemberRepository.save(
                ownerMember
        );


        return toResponse(
                savedGroup
        );
    }


    @Transactional
    public GroupResponse joinPublicGroup(
            Long userId,
            Long groupId,
            GroupJoinRequest request) {

        User user =
                getUser(
                        userId
                );


        DietGroup group =
                dietGroupRepository
                        .findById(
                                groupId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "그룹을 찾을 수 없습니다."
                                )
                        );


        if (group.getVisibility()
                != GroupVisibility.PUBLIC) {

            throw new IllegalArgumentException(
                    "공개 그룹이 아닙니다."
            );
        }


        joinGroup(
                user,
                group,
                request.getGroupNickname()
        );


        return toResponse(
                group
        );
    }


    @Transactional
    public GroupResponse joinPrivateGroup(
            Long userId,
            String inviteCode,
            GroupJoinRequest request) {

        User user =
                getUser(
                        userId
                );


        String normalizedInviteCode =
                inviteCode
                        .trim()
                        .toUpperCase();


        DietGroup group =
                dietGroupRepository
                        .findByInviteCode(
                                normalizedInviteCode
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "초대코드가 올바르지 않습니다."
                                )
                        );


        if (group.getVisibility()
                != GroupVisibility.PRIVATE) {

            throw new IllegalArgumentException(
                    "비공개 그룹이 아닙니다."
            );
        }


        joinGroup(
                user,
                group,
                request.getGroupNickname()
        );


        return toResponse(
                group
        );
    }


    @Transactional(readOnly = true)
    public List<GroupListItemResponse> getPublicGroups() {

        return dietGroupRepository
                .findAllByVisibilityOrderByCreatedAtDesc(
                        GroupVisibility.PUBLIC
                )
                .stream()
                .map(group ->
                        new GroupListItemResponse(
                                group.getId(),
                                group.getName(),
                                group.getDescription(),
                                group.getVisibility(),
                                null,
                                null
                        )
                )
                .toList();
    }


    @Transactional(readOnly = true)
    public List<GroupListItemResponse> getMyGroups(
            Long userId) {

        return groupMemberRepository
                .findAllByUserIdOrderByJoinedAtDesc(
                        userId
                )
                .stream()
                .map(member -> {

                    DietGroup group =
                            member.getGroup();


                    return new GroupListItemResponse(
                            group.getId(),
                            group.getName(),
                            group.getDescription(),
                            group.getVisibility(),
                            member.getGroupNickname(),
                            buildProfileImageUrl(
                                    member
                            )
                    );
                })
                .toList();
    }


    @Transactional(readOnly = true)
    public GroupProfileResponse getMyGroupProfile(
            Long userId,
            Long groupId) {

        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        return toProfileResponse(
                member
        );
    }


    @Transactional
    public GroupProfileResponse updateMyGroupProfile(
            Long userId,
            Long groupId,
            GroupProfileUpdateRequest request) {

        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        String newNickname =
                normalizeGroupNickname(
                        request.getGroupNickname()
                );


        GroupMember nicknameOwner =
                groupMemberRepository
                        .findByGroupIdAndGroupNickname(
                                groupId,
                                newNickname
                        )
                        .orElse(
                                null
                        );


        if (nicknameOwner != null
                && !nicknameOwner
                        .getId()
                        .equals(
                                member.getId()
                        )) {

            throw new IllegalArgumentException(
                    "이미 사용 중인 그룹 닉네임입니다."
            );
        }


        member.updateNickname(
                newNickname
        );


        return toProfileResponse(
                member
        );
    }


    @Transactional
    public GroupProfileImageResponse uploadMyProfileImage(
            Long userId,
            Long groupId,
            MultipartFile file) {

        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        String oldFileName =
                member.getProfileImageUrl();


        String newFileName =
                groupProfileImageService.save(
                        file
                );


        deleteFileIfTransactionRollsBack(
                newFileName
        );


        member.updateProfileImage(
                newFileName
        );


        deleteFileAfterTransactionCommit(
                oldFileName
        );


        return new GroupProfileImageResponse(
                member.getId(),
                groupId,
                member.getGroupNickname(),
                buildProfileImageUrl(
                        member
                )
        );
    }


    @Transactional
    public GroupProfileImageResponse deleteMyProfileImage(
            Long userId,
            Long groupId) {

        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        String oldFileName =
                member.getProfileImageUrl();


        member.removeProfileImage();


        deleteFileAfterTransactionCommit(
                oldFileName
        );


        return new GroupProfileImageResponse(
                member.getId(),
                groupId,
                member.getGroupNickname(),
                null
        );
    }


    private void joinGroup(
            User user,
            DietGroup group,
            String requestedNickname) {

        Long groupId =
                group.getId();

        Long userId =
                user.getId();


        if (groupMemberRepository
                .existsByGroupIdAndUserId(
                        groupId,
                        userId
                )) {

            throw new IllegalArgumentException(
                    "이미 가입한 그룹입니다."
            );
        }


        String groupNickname =
                normalizeGroupNickname(
                        requestedNickname
                );


        if (groupMemberRepository
                .existsByGroupIdAndGroupNickname(
                        groupId,
                        groupNickname
                )) {

            throw new IllegalArgumentException(
                    "이미 사용 중인 그룹 닉네임입니다."
            );
        }


        GroupMember member =
                new GroupMember(
                        group,
                        user,
                        groupNickname,
                        null
                );


        groupMemberRepository.save(
                member
        );
    }


    private GroupMember getGroupMember(
            Long userId,
            Long groupId) {

        return groupMemberRepository
                .findByGroupIdAndUserId(
                        groupId,
                        userId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "가입한 그룹이 아닙니다."
                        )
                );
    }


    private User getUser(
            Long userId) {

        return userRepository
                .findById(
                        userId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        )
                );
    }


    private String normalizeGroupNickname(
            String groupNickname) {

        String normalized =
                groupNickname.trim();


        if (normalized.isBlank()) {

            throw new IllegalArgumentException(
                    "그룹 닉네임은 비어 있을 수 없습니다."
            );
        }


        return normalized;
    }


    private String generateUniqueInviteCode() {

        String inviteCode;


        do {

            inviteCode =
                    generateInviteCode();

        } while (
                dietGroupRepository
                        .findByInviteCode(
                                inviteCode
                        )
                        .isPresent()
        );


        return inviteCode;
    }


    private String generateInviteCode() {

        StringBuilder builder =
                new StringBuilder();


        for (int i = 0;
             i < INVITE_CODE_LENGTH;
             i++) {

            int index =
                    secureRandom.nextInt(
                            INVITE_CODE_CHARACTERS.length()
                    );


            builder.append(
                    INVITE_CODE_CHARACTERS
                            .charAt(
                                    index
                            )
            );
        }


        return builder.toString();
    }


    private String normalizeDescription(
            String description) {

        if (description == null) {

            return null;
        }


        String trimmed =
                description.trim();


        if (trimmed.isBlank()) {

            return null;
        }


        return trimmed;
    }


    private void deleteFileAfterTransactionCommit(
            String fileName) {

        if (fileName == null
                || fileName.isBlank()) {

            return;
        }


        if (!TransactionSynchronizationManager
                .isSynchronizationActive()) {

            safelyDeleteProfileImage(
                    fileName
            );

            return;
        }


        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {

                            @Override
                            public void afterCommit() {

                                safelyDeleteProfileImage(
                                        fileName
                                );
                            }
                        }
                );
    }


    private void deleteFileIfTransactionRollsBack(
            String fileName) {

        if (fileName == null
                || fileName.isBlank()) {

            return;
        }


        if (!TransactionSynchronizationManager
                .isSynchronizationActive()) {

            return;
        }


        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {

                            @Override
                            public void afterCompletion(
                                    int status) {

                                if (status
                                        == TransactionSynchronization.STATUS_ROLLED_BACK) {

                                    safelyDeleteProfileImage(
                                            fileName
                                    );
                                }
                            }
                        }
                );
    }


    private void safelyDeleteProfileImage(
            String fileName) {

        try {

            groupProfileImageService.delete(
                    fileName
            );

        } catch (RuntimeException e) {

            log.warn(
                    "그룹 프로필 이미지 파일 정리에 실패했습니다. fileName={}",
                    fileName,
                    e
            );
        }
    }


    private String buildProfileImageUrl(
            GroupMember member) {

        if (member.getProfileImageUrl() == null
                || member.getProfileImageUrl()
                        .isBlank()) {

            return null;
        }


        return "/api/groups/"
                + member.getGroup().getId()
                + "/members/"
                + member.getId()
                + "/profile/image";
    }


    private GroupResponse toResponse(
            DietGroup group) {

        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getVisibility(),
                group.getInviteCode(),
                group.getOwner().getId(),
                group.getCreatedAt()
        );
    }


    private GroupProfileResponse toProfileResponse(
            GroupMember member) {

        return new GroupProfileResponse(
                member.getId(),
                member.getGroup().getId(),
                member.getGroupNickname(),
                buildProfileImageUrl(
                        member
                )
        );
    }
}