package com.dietmall.ai.client;

import java.net.SocketTimeoutException;
import java.net.http.HttpTimeoutException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;


@Component
public class AiServiceClient {

    private final RestClient restClient;

    public AiServiceClient(
            @Qualifier("aiRestClient")
            RestClient restClient
    ) {
        this.restClient = restClient;
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
            throw new AiServiceException(
                    AiServiceException.Reason.HTTP_ERROR,
                    "AI service returned an HTTP error.",
                    exception.getStatusCode().value(),
                    exception
            );

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
        try {
            R response = restClient
                    .post()
                    .uri(path)
                    .body(requestBody)
                    .retrieve()
                    .body(responseType);

            return requireResponse(response);

        } catch (RestClientResponseException exception) {
            throw new AiServiceException(
                    AiServiceException.Reason.HTTP_ERROR,
                    "AI service returned an HTTP error.",
                    exception.getStatusCode().value(),
                    exception
            );

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

    private <R> R requireResponse(R response) {
        if (response == null) {
            throw new AiServiceException(
                    AiServiceException.Reason.EMPTY_RESPONSE,
                    "AI service returned an empty response.",
                    null
            );
        }

        return response;
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

    private boolean hasTimeoutCause(Throwable throwable) {
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