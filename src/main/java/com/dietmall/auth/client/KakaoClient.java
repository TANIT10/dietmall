package com.dietmall.auth.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dietmall.auth.dto.KakaoUserResponse;

@Component
public class KakaoClient {

    private final RestTemplate restTemplate;

    public KakaoClient(
            RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }


    public KakaoUserResponse getUserInfo(
            String accessToken) {

        String url =
                "https://kapi.kakao.com/v2/user/me";


        HttpHeaders headers = new HttpHeaders();

        headers.set(
                "Authorization",
                "Bearer " + accessToken
        );


        HttpEntity<Void> request =
                new HttpEntity<>(headers);


        ResponseEntity<KakaoUserResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        KakaoUserResponse.class
                );


        return response.getBody();
    }
}