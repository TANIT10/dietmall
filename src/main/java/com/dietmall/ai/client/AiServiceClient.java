package com.dietmall.ai.client;

import java.net.SocketTimeoutException;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;


@Component
public class AiServiceClient {

    private static final int MAX_ERROR_BODY_LENGTH = 1000;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public AiServiceClient(
            @Qualifier("aiRestClient")
            RestClient restClient,
            ObjectMapper objectMapper
    ) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public <R> R get(
            String path,
            Class<R> responseType
    ) {
        try {
            R response = restClient
                    .get()
                    .uri(path)
                    .retrieve()
                    .body(responseType);

            return requireResponse(response);

        } catch (RestClientResponseException exception) {
            throw createHttpException(exception);

        } catch (ResourceAccessException exception) {
            throw createAccessException(exception);

        } catch (RestClientException exception) {
            throw new AiServiceException(
                    AiServiceException.Reason.INVALID_RESPONSE,
                    "AI service response could not be processed.",
                    exception
            );
        }
    }

    public <T, R> R post(
            String path,
            T requestBody,
            Class<R> responseType
    ) {
        byte[] jsonBody = serializeRequest(requestBody);

        try {
            R response = restClient
                    .post()
                    .uri(path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(jsonBody.length)
                    .body(outputStream ->
                            outputStream.write(jsonBody)
                    )
                    .retrieve()
                    .body(responseType);

            return requireResponse(response);

        } catch (RestClientResponseException exception) {
            throw createHttpException(exception);

        } catch (ResourceAccessException exception) {
            throw createAccessException(exception);

        } catch (RestClientException exception) {
            throw new AiServiceException(
                    AiServiceException.Reason.INVALID_RESPONSE,
                    "AI service response could not be processed.",
                    exception
            );
        }
    }

    private byte[] serializeRequest(
            Object requestBody
    ) {
        if (requestBody == null) {
            throw new AiServiceException(
                    AiServiceException.Reason.INVALID_RESPONSE,
                    "AI service request body must not be null.",
                    null
            );
        }

        try {
            String json =
                    objectMapper.writeValueAsString(requestBody);

            return json.getBytes(StandardCharsets.UTF_8);

        } catch (JacksonException exception) {
            throw new AiServiceException(
                    AiServiceException.Reason.INVALID_RESPONSE,
                    "AI service request could not be converted to JSON.",
                    exception
            );
        }
    }

    private <R> R requireResponse(
            R response
    ) {
        if (response == null) {
            throw new AiServiceException(
                    AiServiceException.Reason.EMPTY_RESPONSE,
                    "AI service returned an empty response.",
                    null
            );
        }

        return response;
    }

    private AiServiceException createHttpException(
            RestClientResponseException exception
    ) {
        String responseBody =
                exception.getResponseBodyAsString();

        if (
                responseBody != null
                && responseBody.length()
                > MAX_ERROR_BODY_LENGTH
        ) {
            responseBody = responseBody.substring(
                    0,
                    MAX_ERROR_BODY_LENGTH
            );
        }

        String message =
                "AI service returned HTTP "
                + exception.getStatusCode().value();

        if (
                responseBody != null
                && !responseBody.isBlank()
        ) {
            message += ": " + responseBody;
        }

        return new AiServiceException(
                AiServiceException.Reason.HTTP_ERROR,
                message,
                exception.getStatusCode().value(),
                exception
        );
    }

    private AiServiceException createAccessException(
            ResourceAccessException exception
    ) {
        if (hasTimeoutCause(exception)) {
            return new AiServiceException(
                    AiServiceException.Reason.TIMEOUT,
                    "AI service request timed out.",
                    exception
            );
        }

        return new AiServiceException(
                AiServiceException.Reason.CONNECTION_FAILED,
                "AI service connection failed.",
                exception
        );
    }

    private boolean hasTimeoutCause(
            Throwable throwable
    ) {
        Throwable current = throwable;

        while (current != null) {
            if (
                    current instanceof HttpTimeoutException
                    || current instanceof SocketTimeoutException
            ) {
                return true;
            }

            current = current.getCause();
        }

        return false;
    }
}