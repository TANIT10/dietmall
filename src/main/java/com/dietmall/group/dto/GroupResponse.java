package com.dietmall.group.dto;

import java.time.Instant;

import com.dietmall.group.entity.GroupVisibility;

public class GroupResponse {

    private final Long groupId;
    private final String name;
    private final String description;
    private final GroupVisibility visibility;
    private final String inviteCode;
    private final Long ownerUserId;
    private final Instant createdAt;


    public GroupResponse(
            Long groupId,
            String name,
            String description,
            GroupVisibility visibility,
            String inviteCode,
            Long ownerUserId,
            Instant createdAt) {

        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.inviteCode = inviteCode;
        this.ownerUserId = ownerUserId;
        this.createdAt = createdAt;
    }


    public Long getGroupId() {
        return groupId;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public GroupVisibility getVisibility() {
        return visibility;
    }


    public String getInviteCode() {
        return inviteCode;
    }


    public Long getOwnerUserId() {
        return ownerUserId;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }
}