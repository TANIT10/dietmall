package com.dietmall.global.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.dietmall.auth.exception.DuplicateEmailException;
import com.dietmall.auth.exception.InvalidCredentialsException;
import com.dietmall.auth.exception.InvalidRefreshTokenException;
import com.dietmall.user.exception.OnboardingAlreadyCompletedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 중복 이메일
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmail(
            DuplicateEmailException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        Map.of(
                                "code", "DUPLICATE_EMAIL",
                                "message", e.getMessage()
                        )
                );
    }

    // 로그인 실패
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(
            InvalidCredentialsException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        Map.of(
                                "code", "INVALID_CREDENTIALS",
                                "message", e.getMessage()
                        )
                );
    }

    // Refresh Token 실패
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRefreshToken(
            InvalidRefreshTokenException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        Map.of(
                                "code", "INVALID_REFRESH_TOKEN",
                                "message", e.getMessage()
                        )
                );
    }

    // 이미 온보딩을 완료한 사용자
    @ExceptionHandler(OnboardingAlreadyCompletedException.class)
    public ResponseEntity<Map<String, String>> handleOnboardingAlreadyCompleted(
            OnboardingAlreadyCompletedException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        Map.of(
                                "code", "ONBOARDING_ALREADY_COMPLETED",
                                "message", e.getMessage()
                        )
                );
    }

    // @RequestBody @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return createValidationResponse(errors);
    }

    // Spring 메서드 검증 실패
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleMethodValidation(
            HandlerMethodValidationException e) {

        Map<String, String> errors = new LinkedHashMap<>();

        e.getParameterValidationResults().forEach(result -> {

            if (result instanceof ParameterErrors parameterErrors) {

                parameterErrors.getFieldErrors().forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );
            }
        });

        return createValidationResponse(errors);
    }

    private ResponseEntity<Map<String, Object>> createValidationResponse(
            Map<String, String> errors) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("code", "VALIDATION_ERROR");
        response.put("message", "입력값을 확인해주세요.");
        response.put("errors", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}