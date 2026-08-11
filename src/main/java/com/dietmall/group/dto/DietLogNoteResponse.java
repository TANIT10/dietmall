package com.dietmall.group.dto;

import java.time.Instant;

public class DietLogNoteResponse {

    private final Long noteId;
    private final Long logId;
    private final Long authorMemberId;
    private final String authorNickname;
    private final String content;
    private final Instant createdAt;
    private final Instant updatedAt;


    public DietLogNoteResponse(
            Long noteId,
            Long logId,
            Long authorMemberId,
            String authorNickname,
            String content,
            Instant createdAt,
            Instant updatedAt) {

        this.noteId = noteId;
        this.logId = logId;
        this.authorMemberId = authorMemberId;
        this.authorNickname = authorNickname;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Long getNoteId() {
        return noteId;
    }


    public Long getLogId() {
        return logId;
    }


    public Long getAuthorMemberId() {
        return authorMemberId;
    }


    public String getAuthorNickname() {
        return authorNickname;
    }


    public String getContent() {
        return content;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }


    public Instant getUpdatedAt() {
        return updatedAt;
    }
}