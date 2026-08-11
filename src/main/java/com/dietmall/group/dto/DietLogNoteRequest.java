package com.dietmall.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DietLogNoteRequest {

    @NotBlank(
            message = "포스트잇 내용은 필수입니다."
    )
    @Size(
            max = 20,
            message = "포스트잇은 20자 이하여야 합니다."
    )
    private String content;


    public DietLogNoteRequest() {
    }


    public String getContent() {
        return content;
    }


    public void setContent(
            String content) {

        this.content = content;
    }
}