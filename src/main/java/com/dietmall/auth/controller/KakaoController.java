package com.dietmall.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.auth.dto.KakaoLoginRequest;
import com.dietmall.auth.dto.LoginResponse;
import com.dietmall.auth.service.KakaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping
public class KakaoController {

    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }


    /*
     * 모바일/프론트에서 카카오 access token을
     * 직접 보내는 경우 사용하는 API
     */
    @PostMapping("/api/auth/kakao/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody KakaoLoginRequest request) {

        LoginResponse response =
                kakaoService.login(request.getAccessToken());

        return ResponseEntity.ok(response);
    }


    /*
     * 카카오 로그인 후
     * redirect URI로 전달되는 authorization code를 받는 API
     */
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<LoginResponse> callback(
            @RequestParam("code") String code) {

        LoginResponse response =
                kakaoService.loginWithCode(code);

        return ResponseEntity.ok(response);
    }
}