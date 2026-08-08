package com.dietmall.user.exception;

public class OnboardingAlreadyCompletedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OnboardingAlreadyCompletedException() {
        super("이미 온보딩을 완료한 사용자입니다.");
    }
}