package com.dietmall.group.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.group.dto.DietLogNoteRequest;
import com.dietmall.group.dto.DietLogNoteResponse;
import com.dietmall.group.entity.DietLog;
import com.dietmall.group.entity.DietLogNote;
import com.dietmall.group.entity.GroupMember;
import com.dietmall.group.repository.DietLogNoteRepository;
import com.dietmall.group.repository.DietLogRepository;
import com.dietmall.group.repository.GroupMemberRepository;

@Service
public class DietLogNoteService {

    private final DietLogNoteRepository dietLogNoteRepository;
    private final DietLogRepository dietLogRepository;
    private final GroupMemberRepository groupMemberRepository;


    public DietLogNoteService(
            DietLogNoteRepository dietLogNoteRepository,
            DietLogRepository dietLogRepository,
            GroupMemberRepository groupMemberRepository) {

        this.dietLogNoteRepository =
                dietLogNoteRepository;

        this.dietLogRepository =
                dietLogRepository;

        this.groupMemberRepository =
                groupMemberRepository;
    }


    @Transactional
    public DietLogNoteResponse createNote(
            Long userId,
            Long groupId,
            Long logId,
            DietLogNoteRequest request) {

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


        if (log.getAuthor()
                .getId()
                .equals(member.getId())) {

            throw new IllegalArgumentException(
                    "자신이 작성한 로그에는 포스트잇을 작성할 수 없습니다."
            );
        }


        if (dietLogNoteRepository
                .existsByLogIdAndAuthorId(
                        logId,
                        member.getId()
                )) {

            throw new IllegalArgumentException(
                    "이 로그에는 이미 포스트잇을 작성했습니다."
            );
        }


        DietLogNote note =
                new DietLogNote(
                        log,
                        member,
                        normalizeContent(
                                request.getContent()
                        )
                );


        DietLogNote savedNote =
                dietLogNoteRepository.save(
                        note
                );


        return toResponse(
                savedNote
        );
    }


    @Transactional(readOnly = true)
    public List<DietLogNoteResponse> getNotes(
            Long userId,
            Long groupId,
            Long logId) {

        getGroupMember(
                userId,
                groupId
        );


        getDietLog(
                groupId,
                logId
        );


        return dietLogNoteRepository
                .findAllByLogIdOrderByCreatedAtAsc(
                        logId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional
    public DietLogNoteResponse updateNote(
            Long userId,
            Long groupId,
            Long logId,
            Long noteId,
            DietLogNoteRequest request) {

        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        getDietLog(
                groupId,
                logId
        );


        DietLogNote note =
                getNote(
                        noteId
                );


        validateNoteBelongsToLog(
                note,
                logId
        );


        validateNoteOwner(
                note,
                member
        );


        note.updateContent(
                normalizeContent(
                        request.getContent()
                )
        );


        return toResponse(
                note
        );
    }


    @Transactional
    public void deleteNote(
            Long userId,
            Long groupId,
            Long logId,
            Long noteId) {

        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        getDietLog(
                groupId,
                logId
        );


        DietLogNote note =
                getNote(
                        noteId
                );


        validateNoteBelongsToLog(
                note,
                logId
        );


        validateNoteOwner(
                note,
                member
        );


        dietLogNoteRepository.delete(
                note
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


    private DietLogNote getNote(
            Long noteId) {

        return dietLogNoteRepository
                .findById(noteId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "포스트잇을 찾을 수 없습니다."
                        )
                );
    }


    private void validateNoteBelongsToLog(
            DietLogNote note,
            Long logId) {

        if (!note.getLog()
                .getId()
                .equals(logId)) {

            throw new IllegalArgumentException(
                    "해당 로그의 포스트잇이 아닙니다."
            );
        }
    }


    private void validateNoteOwner(
            DietLogNote note,
            GroupMember member) {

        if (!note.getAuthor()
                .getId()
                .equals(member.getId())) {

            throw new IllegalArgumentException(
                    "본인이 작성한 포스트잇만 수정하거나 삭제할 수 있습니다."
            );
        }
    }


    private String normalizeContent(
            String content) {

        String trimmed =
                content.trim();


        if (trimmed.isBlank()) {

            throw new IllegalArgumentException(
                    "포스트잇 내용은 필수입니다."
            );
        }


        if (trimmed.length() > 20) {

            throw new IllegalArgumentException(
                    "포스트잇은 20자 이하여야 합니다."
            );
        }


        return trimmed;
    }


    private DietLogNoteResponse toResponse(
            DietLogNote note) {

        return new DietLogNoteResponse(
                note.getId(),
                note.getLog().getId(),
                note.getAuthor().getId(),
                note.getAuthor()
                        .getGroupNickname(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
}