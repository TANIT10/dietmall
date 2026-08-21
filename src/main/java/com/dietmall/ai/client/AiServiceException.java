package com.dietmall.ai.client;


public class AiServiceException extends RuntimeException {

    private final Reason reason;
    private final Integer statusCode;

    public AiServiceException(
            Reason reason,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.reason = reason;
        this.statusCode = null;
    }

    public AiServiceException(
            Reason reason,
            String message,
            Integer statusCode,
            Throwable cause
    ) {
        super(message, cause);
        this.reason = reason;
        this.statusCode = statusCode;
    }

    public Reason getReason() {
        return reason;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public enum Reason {
        CONNECTION_FAILED,
        TIMEOUT,
        HTTP_ERROR,
        EMPTY_RESPONSE,
        INVALID_RESPONSE
    }
}