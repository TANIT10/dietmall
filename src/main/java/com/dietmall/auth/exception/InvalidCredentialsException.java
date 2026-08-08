package com.dietmall.auth.exception;

public class InvalidCredentialsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidCredentialsException() {
        super("이메일 또는 비밀번호가 올바르지 않습니다.");
    }
}