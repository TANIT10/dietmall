package com.dietmall.group.dto;

import java.time.Instant;

public class DietLogResponse {

    private final Long logId;
    private final Long groupId;

    private final Long authorMemberId;
    private final String authorNickname;
    private final String authorProfileImageUrl;

    private final String imageUrl;
    private final String memo;

    private final Instant createdAt;
    private final Instant updatedAt;


    public DietLogResponse(
            Long logId,
            Long groupId,
            Long authorMemberId,
            String authorNickname,
            String authorProfileImageUrl,
            String imageUrl,
            String memo,
            Instant createdAt,
            Instant updatedAt) {

        this.logId = logId;
        this.groupId = groupId;
        this.authorMemberId = authorMemberId;
        this.authorNickname = authorNickname;
        this.authorProfileImageUrl = authorProfileImageUrl;
        this.imageUrl = imageUrl;
        this.memo = memo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Long getLogId() {
        return logId;
    }


    public Long getGroupId() {
        return groupId;
    }


    public Long getAuthorMemberId() {
        return authorMemberId;
    }


    public String getAuthorNickname() {
        return authorNickname;
    }


    public String getAuthorProfileImageUrl() {
        return authorProfileImageUrl;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    public String getMemo() {
        return memo;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }


    public Instant getUpdatedAt() {
        return updatedAt;
    }
}