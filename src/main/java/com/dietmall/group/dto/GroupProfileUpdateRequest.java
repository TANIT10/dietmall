package com.dietmall.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class GroupProfileUpdateRequest {

    @NotBlank(message = "그룹 닉네임은 필수입니다.")
    @Size(
            max = 30,
            message = "그룹 닉네임은 30자 이하여야 합니다."
    )
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]+$",
            message = "그룹 닉네임은 한글, 영어, 숫자만 사용할 수 있습니다."
    )
    private String groupNickname;


    public GroupProfileUpdateRequest() {
    }


    public String getGroupNickname() {
        return groupNickname;
    }


    public void setGroupNickname(
            String groupNickname) {

        this.groupNickname =
                groupNickname;
    }
}