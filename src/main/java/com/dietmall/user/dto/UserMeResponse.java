package com.dietmall.user.dto;

public class UserMeResponse {

    private final Long userId;
    private final String nickname;
    private final boolean onboardingCompleted;

    public UserMeResponse(
            Long userId,
            String nickname,
            boolean onboardingCompleted) {

        this.userId = userId;
        this.nickname = nickname;
        this.onboardingCompleted = onboardingCompleted;
    }

    public Long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isOnboardingCompleted() {
        return onboardingCompleted;
    }
}