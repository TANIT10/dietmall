package com.dietmall.group.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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

    private static final Logger log =
            LoggerFactory.getLogger(
                    DietLogService.class
            );


    private final DietLogRepository dietLogRepository;
    private final DietGroupRepository dietGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final DietLogImageService dietLogImageService;


    public DietLogService(
            DietLogRepository dietLogRepository,
            DietGroupRepository dietGroupRepository,
            GroupMemberRepository groupMemberRepository,
            DietLogImageService dietLogImageService) {

        this.dietLogRepository =
                dietLogRepository;

        this.dietGroupRepository =
                dietGroupRepository;

        this.groupMemberRepository =
                groupMemberRepository;

        this.dietLogImageService =
                dietLogImageService;
    }


    @Transactional
    public DietLogResponse createLog(
            Long userId,
            Long groupId,
            DietLogCreateRequest request,
            MultipartFile file) {

        DietGroup group =
                getGroup(
                        groupId
                );


        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        String savedFileName =
                dietLogImageService.save(
                        file
                );


        deleteFileIfTransactionRollsBack(
                savedFileName
        );


        DietLog logEntity =
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
                        logEntity
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
                .map(
                        this::toResponse
                )
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


        DietLog logEntity =
                getDietLog(
                        groupId,
                        logId
                );


        return toResponse(
                logEntity
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


        DietLog logEntity =
                getDietLog(
                        groupId,
                        logId
                );


        validateLogOwner(
                member,
                logEntity
        );


        logEntity.updateMemo(
                normalizeMemo(
                        request.getMemo()
                )
        );


        return toResponse(
                logEntity
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


        DietLog logEntity =
                getDietLog(
                        groupId,
                        logId
                );


        validateLogOwner(
                member,
                logEntity
        );


        String oldFileName =
                logEntity.getImageUrl();


        String newFileName =
                dietLogImageService.save(
                        file
                );


        deleteFileIfTransactionRollsBack(
                newFileName
        );


        logEntity.updateImage(
                newFileName
        );


        deleteFileAfterTransactionCommit(
                oldFileName
        );


        return toResponse(
                logEntity
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


        DietLog logEntity =
                getDietLog(
                        groupId,
                        logId
                );


        validateLogOwner(
                member,
                logEntity
        );


        String fileName =
                logEntity.getImageUrl();


        dietLogRepository.delete(
                logEntity
        );


        deleteFileAfterTransactionCommit(
                fileName
        );
    }


    private DietGroup getGroup(
            Long groupId) {

        return dietGroupRepository
                .findById(
                        groupId
                )
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
            DietLog logEntity) {

        if (!logEntity
                .getAuthor()
                .getId()
                .equals(
                        member.getId()
                )) {

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


    private void deleteFileAfterTransactionCommit(
            String fileName) {

        if (fileName == null
                || fileName.isBlank()) {

            return;
        }


        if (!TransactionSynchronizationManager
                .isSynchronizationActive()) {

            safelyDeleteFile(
                    fileName
            );

            return;
        }


        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {

                            @Override
                            public void afterCommit() {

                                safelyDeleteFile(
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

                                    safelyDeleteFile(
                                            fileName
                                    );
                                }
                            }
                        }
                );
    }


    private void safelyDeleteFile(
            String fileName) {

        try {

            dietLogImageService.delete(
                    fileName
            );

        } catch (RuntimeException e) {

            log.warn(
                    "다이어트 로그 이미지 파일 정리에 실패했습니다. fileName={}",
                    fileName,
                    e
            );
        }
    }


    private DietLogResponse toResponse(
            DietLog logEntity) {

        GroupMember author =
                logEntity.getAuthor();


        return new DietLogResponse(
                logEntity.getId(),
                logEntity.getGroup().getId(),
                author.getId(),
                author.getGroupNickname(),
                buildProfileImageUrl(
                        author
                ),
                buildLogImageUrl(
                        logEntity
                ),
                logEntity.getMemo(),
                logEntity.getCreatedAt(),
                logEntity.getUpdatedAt()
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
            DietLog logEntity) {

        return "/api/groups/"
                + logEntity.getGroup().getId()
                + "/logs/"
                + logEntity.getId()
                + "/image";
    }
}