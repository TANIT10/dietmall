package com.dietmall.auth.dto;

public class LoginResponse {

    private final Long userId;
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;

    public LoginResponse(
            Long userId,
            String accessToken,
            String refreshToken) {

        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
    }

    public Long getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}