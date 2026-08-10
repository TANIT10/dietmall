package com.dietmall.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class KakaoLoginRequest {

    @NotBlank(message = "카카오 액세스 토큰은 필수입니다.")
    private String accessToken;

    public KakaoLoginRequest() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}