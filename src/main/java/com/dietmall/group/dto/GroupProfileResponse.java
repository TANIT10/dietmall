package com.dietmall.group.dto;

public class GroupProfileResponse {

    private final Long memberId;
    private final Long groupId;
    private final String groupNickname;
    private final String profileImageUrl;


    public GroupProfileResponse(
            Long memberId,
            Long groupId,
            String groupNickname,
            String profileImageUrl) {

        this.memberId = memberId;
        this.groupId = groupId;
        this.groupNickname = groupNickname;
        this.profileImageUrl = profileImageUrl;
    }


    public Long getMemberId() {
        return memberId;
    }


    public Long getGroupId() {
        return groupId;
    }


    public String getGroupNickname() {
        return groupNickname;
    }


    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}