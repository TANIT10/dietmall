package com.dietmall.ai.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;


@Configuration
@EnableConfigurationProperties(AiServiceProperties.class)
public class AiClientConfig {

    @Bean
    @Qualifier("aiRestClient")
    public RestClient aiRestClient(
            AiServiceProperties properties
    ) {
        SimpleClientHttpRequestFactory requestFactory =
                new SimpleClientHttpRequestFactory();

        requestFactory.setConnectTimeout(
                properties.connectTimeout()
        );

        requestFactory.setReadTimeout(
                properties.readTimeout()
        );

        RestClient.Builder aiClientBuilder = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(requestFactory)
                .messageConverters(converters ->
                        converters.add(
                                0,
                                new JacksonJsonHttpMessageConverter()
                        )
                )
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .defaultHeader(
                        HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON_VALUE
                );

        if (
                properties.apiKey() != null
                && !properties.apiKey().isBlank()
        ) {
            aiClientBuilder.defaultHeader(
                    "X-Internal-Api-Key",
                    properties.apiKey()
            );
        }

        return aiClientBuilder.build();
    }
}