package com.dietmall.group.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dietmall.group.dto.DietLogCreateRequest;
import com.dietmall.group.dto.DietLogResponse;
import com.dietmall.group.dto.DietLogUpdateRequest;
import com.dietmall.group.entity.DietGroup;
import com.dietmall.group.entity.DietLog;
import com.dietmall.group.entity.GroupMember;
import com.dietmall.group.repository.DietGroupRepository;
import com.dietmall.group.repository.DietLogRepository;
import com.dietmall.group.repository.GroupMemberRepository;

@Service
public class DietLogService {

    private final DietLogRepository dietLogRepository;
    private final DietGroupRepository dietGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final DietLogImageService dietLogImageService;


    public DietLogService(
            DietLogRepository dietLogRepository,
            DietGroupRepository dietGroupRepository,
            GroupMemberRepository groupMemberRepository,
            DietLogImageService dietLogImageService) {

        this.dietLogRepository = dietLogRepository;
        this.dietGroupRepository = dietGroupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.dietLogImageService = dietLogImageService;
    }


    @Transactional
    public DietLogResponse createLog(
            Long userId,
            Long groupId,
            DietLogCreateRequest request,
            MultipartFile file) {

        DietGroup group =
                getGroup(groupId);


        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        String savedFileName =
                dietLogImageService.save(
                        file
                );


        DietLog log =
                new DietLog(
                        group,
                        member,
                        savedFileName,
                        normalizeMemo(
                                request.getMemo()
                        )
                );


        DietLog savedLog =
                dietLogRepository.save(
                        log
                );


        return toResponse(
                savedLog
        );
    }


    @Transactional(readOnly = true)
    public List<DietLogResponse> getGroupLogs(
            Long userId,
            Long groupId) {

        getGroupMember(
                userId,
                groupId
        );


        return dietLogRepository
                .findAllByGroupIdOrderByCreatedAtDesc(
                        groupId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public DietLogResponse getLog(
            Long userId,
            Long groupId,
            Long logId) {

        getGroupMember(
                userId,
                groupId
        );


        DietLog log =
                getDietLog(
                        groupId,
                        logId
                );


        return toResponse(
                log
        );
    }


    @Transactional
    public DietLogResponse updateMemo(
            Long userId,
            Long groupId,
            Long logId,
            DietLogUpdateRequest request) {

        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        DietLog log =
                getDietLog(
                        groupId,
                        logId
                );


        validateLogOwner(
                member,
                log
        );


        log.updateMemo(
                normalizeMemo(
                        request.getMemo()
                )
        );


        return toResponse(
                log
        );
    }


    @Transactional
    public DietLogResponse updateImage(
            Long userId,
            Long groupId,
            Long logId,
            MultipartFile file) {

        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        DietLog log =
                getDietLog(
                        groupId,
                        logId
                );


        validateLogOwner(
                member,
                log
        );


        String oldFileName =
                log.getImageUrl();


        String newFileName =
                dietLogImageService.save(
                        file
                );


        log.updateImage(
                newFileName
        );


        dietLogImageService.delete(
                oldFileName
        );


        return toResponse(
                log
        );
    }


    @Transactional
    public void deleteLog(
            Long userId,
            Long groupId,
            Long logId) {

        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        DietLog log =
                getDietLog(
                        groupId,
                        logId
                );


        validateLogOwner(
                member,
                log
        );


        String fileName =
                log.getImageUrl();


        dietLogRepository.delete(
                log
        );


        dietLogImageService.delete(
                fileName
        );
    }


    private DietGroup getGroup(
            Long groupId) {

        return dietGroupRepository
                .findById(groupId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "그룹을 찾을 수 없습니다."
                        )
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


    private DietLog getDietLog(
            Long groupId,
            Long logId) {

        return dietLogRepository
                .findByIdAndGroupId(
                        logId,
                        groupId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "다이어트 로그를 찾을 수 없습니다."
                        )
                );
    }


    private void validateLogOwner(
            GroupMember member,
            DietLog log) {

        if (!log.getAuthor()
                .getId()
                .equals(member.getId())) {

            throw new IllegalArgumentException(
                    "본인이 작성한 로그만 수정하거나 삭제할 수 있습니다."
            );
        }
    }


    private String normalizeMemo(
            String memo) {

        if (memo == null) {
            return null;
        }


        String trimmed =
                memo.trim();


        if (trimmed.isBlank()) {
            return null;
        }


        return trimmed;
    }


    private DietLogResponse toResponse(
            DietLog log) {

        GroupMember author =
                log.getAuthor();


        return new DietLogResponse(
                log.getId(),
                log.getGroup().getId(),
                author.getId(),
                author.getGroupNickname(),
                buildProfileImageUrl(author),
                buildLogImageUrl(log),
                log.getMemo(),
                log.getCreatedAt(),
                log.getUpdatedAt()
        );
    }


    private String buildProfileImageUrl(
            GroupMember member) {

        if (member.getProfileImageUrl() == null
                || member.getProfileImageUrl().isBlank()) {

            return null;
        }


        return "/api/groups/"
                + member.getGroup().getId()
                + "/members/"
                + member.getId()
                + "/profile/image";
    }


    private String buildLogImageUrl(
            DietLog log) {

        return "/api/groups/"
                + log.getGroup().getId()
                + "/logs/"
                + log.getId()
                + "/image";
    }
}