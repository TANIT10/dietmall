package com.dietmall.auth.exception;

public class InvalidRefreshTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidRefreshTokenException() {
        super("유효하지 않거나 만료된 Refresh Token입니다.");
    }
}