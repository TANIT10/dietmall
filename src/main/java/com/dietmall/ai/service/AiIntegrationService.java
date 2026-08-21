package com.dietmall.ai.service;

import org.springframework.stereotype.Service;

import com.dietmall.ai.client.AiHealthResponse;
import com.dietmall.ai.client.AiServiceClient;
import com.dietmall.ai.client.AiServiceException;


@Service
public class AiIntegrationService {

    private static final String HEALTH_PATH = "/health";

    private final AiServiceClient aiServiceClient;

    public AiIntegrationService(
            AiServiceClient aiServiceClient
    ) {
        this.aiServiceClient = aiServiceClient;
    }

    public AiHealthResponse getHealth() {
        return aiServiceClient.get(
                HEALTH_PATH,
                AiHealthResponse.class
        );
    }

    public boolean isAvailable() {
        try {
            return getHealth().isHealthy();

        } catch (AiServiceException exception) {
            return false;
        }
    }
}