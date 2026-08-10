package com.dietmall.auth.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.auth.dto.LoginResponse;
import com.dietmall.auth.entity.AuthAccount;
import com.dietmall.auth.entity.AuthProvider;
import com.dietmall.auth.jwt.JwtTokenService;
import com.dietmall.auth.repository.AuthAccountRepository;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.UserRepository;

@Service
public class GoogleService {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final UserRepository userRepository;
    private final AuthAccountRepository authAccountRepository;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;


    public GoogleService(
            GoogleTokenVerifier googleTokenVerifier,
            UserRepository userRepository,
            AuthAccountRepository authAccountRepository,
            JwtTokenService jwtTokenService,
            RefreshTokenService refreshTokenService) {

        this.googleTokenVerifier = googleTokenVerifier;
        this.userRepository = userRepository;
        this.authAccountRepository = authAccountRepository;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
    }


    @Transactional
    public LoginResponse login(String idToken) {

        // 1. 구글 ID 토큰이 진짜인지 검사
        Jwt googleJwt =
                googleTokenVerifier.verify(idToken);


        // 2. 구글 사용자의 고유 ID
        String providerUserId =
                googleJwt.getSubject();


        if (providerUserId == null
                || providerUserId.isBlank()) {

            throw new IllegalStateException(
                    "구글 사용자 ID를 확인할 수 없습니다."
            );
        }


        // 3. 구글이 제공한 사용자 정보
        String email =
                googleJwt.getClaimAsString("email");

        String nickname =
                googleJwt.getClaimAsString("name");


        // 4. 이미 가입한 구글 사용자인지 확인
        AuthAccount authAccount =
                authAccountRepository
                        .findByProviderAndProviderUserId(
                                AuthProvider.GOOGLE,
                                providerUserId
                        )
                        .orElse(null);


        User user;


        // 기존 구글 회원
        if (authAccount != null) {

            user = authAccount.getUser();

        } else {

            // 이름을 못 받은 경우를 대비한 기본 닉네임
            if (nickname == null || nickname.isBlank()) {
                nickname = "구글 사용자";
            }


            // 5. 신규 User 생성
            User savedUser =
                    userRepository.save(
                            new User(nickname)
                    );


            // 6. GOOGLE 로그인 계정 생성
            AuthAccount newAccount =
                    new AuthAccount(
                            savedUser,
                            AuthProvider.GOOGLE,
                            providerUserId,
                            email,
                            null
                    );


            authAccountRepository.save(newAccount);

            user = savedUser;
        }


        // 7. DietMall JWT 발급
        Long userId = user.getId();


        String newAccessToken =
                jwtTokenService.createAccessToken(userId);


        String newRefreshToken =
                refreshTokenService.createRefreshToken(userId);


        // 8. 일반 로그인 / 카카오 로그인과 똑같은 형태로 반환
        return new LoginResponse(
                userId,
                newAccessToken,
                newRefreshToken
        );
    }
}