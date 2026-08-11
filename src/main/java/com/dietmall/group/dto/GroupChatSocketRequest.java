package com.dietmall.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GroupChatSocketRequest {

    @NotBlank(
            message = "채팅 메시지는 필수입니다."
    )
    @Size(
            max = 500,
            message = "채팅 메시지는 500자 이하여야 합니다."
    )
    private String content;


    public GroupChatSocketRequest() {
    }


    public String getContent() {
        return content;
    }


    public void setContent(
            String content) {

        this.content = content;
    }
}