package com.dietmall.ai.client;


public record AiHealthResponse(
        String status,
        String service,
        String version,
        String environment
) {
    public boolean isHealthy() {
        return "ok".equalsIgnoreCase(status);
    }
}