package com.dietmall.ai.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CoachChatRequest(

        @NotBlank(
                message = "AI PT에게 보낼 질문을 입력해주세요."
        )
        @Size(
                max = 1000,
                message = "질문은 1000자 이하로 입력해주세요."
        )
        String message,

        @Valid
        @Size(
                max = 20,
                message = "최근 대화 내용은 최대 20개까지 전달할 수 있습니다."
        )
        List<ConversationMessage> conversationHistory

) {

    public record ConversationMessage(

            @NotBlank(
                    message = "대화 역할이 없습니다."
            )
            @Pattern(
                    regexp = "USER|ASSISTANT",
                    message = "대화 역할은 USER 또는 ASSISTANT여야 합니다."
            )
            String role,

            @NotBlank(
                    message = "대화 내용이 없습니다."
            )
            @Size(
                    max = 2000,
                    message = "대화 내용은 2000자 이하로 입력해주세요."
            )
            String content

    ) {
    }
}