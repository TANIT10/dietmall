package com.dietmall.group.dto;

import com.dietmall.group.entity.GroupVisibility;

public class GroupListItemResponse {

    private final Long groupId;
    private final String name;
    private final String description;
    private final GroupVisibility visibility;
    private final String groupNickname;
    private final String profileImageUrl;


    public GroupListItemResponse(
            Long groupId,
            String name,
            String description,
            GroupVisibility visibility,
            String groupNickname,
            String profileImageUrl) {

        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.groupNickname = groupNickname;
        this.profileImageUrl = profileImageUrl;
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


    public String getGroupNickname() {
        return groupNickname;
    }


    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}