package com.dietmall.auth.exception;

public class DuplicateEmailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateEmailException() {
        super("이미 가입된 이메일입니다.");
    }
}