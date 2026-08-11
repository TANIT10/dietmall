package com.dietmall.group.dto;

import com.dietmall.group.entity.GroupVisibility;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class GroupCreateRequest {

    @NotBlank(message = "그룹 이름은 필수입니다.")
    @Size(
            max = 50,
            message = "그룹 이름은 50자 이하여야 합니다."
    )
    private String name;


    @Size(
            max = 200,
            message = "그룹 설명은 200자 이하여야 합니다."
    )
    private String description;


    @NotNull(message = "그룹 공개 여부는 필수입니다.")
    private GroupVisibility visibility;


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


    public GroupCreateRequest() {
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public GroupVisibility getVisibility() {
        return visibility;
    }


    public void setVisibility(
            GroupVisibility visibility) {

        this.visibility = visibility;
    }


    public String getGroupNickname() {
        return groupNickname;
    }


    public void setGroupNickname(
            String groupNickname) {

        this.groupNickname = groupNickname;
    }
}