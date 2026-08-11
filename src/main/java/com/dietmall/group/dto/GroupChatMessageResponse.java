package com.dietmall.group.dto;

import java.time.Instant;

public class GroupChatMessageResponse {

    private final Long messageId;
    private final Long groupId;

    private final Long authorMemberId;
    private final String authorNickname;
    private final String authorProfileImageUrl;

    private final String content;
    private final Instant createdAt;


    public GroupChatMessageResponse(
            Long messageId,
            Long groupId,
            Long authorMemberId,
            String authorNickname,
            String authorProfileImageUrl,
            String content,
            Instant createdAt) {

        this.messageId = messageId;
        this.groupId = groupId;
        this.authorMemberId = authorMemberId;
        this.authorNickname = authorNickname;
        this.authorProfileImageUrl = authorProfileImageUrl;
        this.content = content;
        this.createdAt = createdAt;
    }


    public Long getMessageId() {
        return messageId;
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


    public String getContent() {
        return content;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }
}