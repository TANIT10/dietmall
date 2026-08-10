package com.dietmall.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.auth.client.KakaoClient;
import com.dietmall.auth.client.KakaoTokenClient;
import com.dietmall.auth.dto.KakaoTokenResponse;
import com.dietmall.auth.dto.KakaoUserResponse;
import com.dietmall.auth.dto.LoginResponse;
import com.dietmall.auth.entity.AuthAccount;
import com.dietmall.auth.entity.AuthProvider;
import com.dietmall.auth.jwt.JwtTokenService;
import com.dietmall.auth.repository.AuthAccountRepository;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.UserRepository;

@Service
public class KakaoService {

    private final KakaoClient kakaoClient;
    private final KakaoTokenClient kakaoTokenClient;
    private final UserRepository userRepository;
    private final AuthAccountRepository authAccountRepository;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;


    public KakaoService(
            KakaoClient kakaoClient,
            KakaoTokenClient kakaoTokenClient,
            UserRepository userRepository,
            AuthAccountRepository authAccountRepository,
            JwtTokenService jwtTokenService,
            RefreshTokenService refreshTokenService) {

        this.kakaoClient = kakaoClient;
        this.kakaoTokenClient = kakaoTokenClient;
        this.userRepository = userRepository;
        this.authAccountRepository = authAccountRepository;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
    }


    /*
     * 카카오 authorization code를 받아서
     * 카카오 access token으로 바꾼 뒤 로그인한다.
     */
    @Transactional
    public LoginResponse loginWithCode(String authorizationCode) {

        KakaoTokenResponse tokenResponse =
                kakaoTokenClient.getToken(authorizationCode);

        if (tokenResponse == null
                || tokenResponse.getAccessToken() == null
                || tokenResponse.getAccessToken().isBlank()) {

            throw new IllegalStateException(
                    "카카오 액세스 토큰을 발급받지 못했습니다."
            );
        }

        return login(tokenResponse.getAccessToken());
    }


    /*
     * 카카오 access token을 이용해서
     * 실제 DietMall 로그인을 처리한다.
     */
    @Transactional
    public LoginResponse login(String accessToken) {

        KakaoUserResponse kakaoUser =
                kakaoClient.getUserInfo(accessToken);

        if (kakaoUser == null || kakaoUser.getId() == null) {
            throw new IllegalStateException(
                    "카카오 사용자 정보를 가져오지 못했습니다."
            );
        }

        String providerUserId =
                String.valueOf(kakaoUser.getId());


        AuthAccount authAccount =
                authAccountRepository
                        .findByProviderAndProviderUserId(
                                AuthProvider.KAKAO,
                                providerUserId
                        )
                        .orElse(null);


        User user;


        // 기존 카카오 회원
        if (authAccount != null) {

            user = authAccount.getUser();

        } else {

            String nickname = null;

            if (kakaoUser.getProperties() != null) {
                nickname =
                        kakaoUser
                                .getProperties()
                                .getNickname();
            }


            // 신규 카카오 회원 생성
            user = new User(nickname);


            User savedUser =
                    userRepository.save(user);


            AuthAccount newAccount =
                    new AuthAccount(
                            savedUser,
                            AuthProvider.KAKAO,
                            providerUserId,
                            null,
                            null
                    );


            authAccountRepository.save(newAccount);

            user = savedUser;
        }


        Long userId = user.getId();


        String newAccessToken =
                jwtTokenService.createAccessToken(userId);


        String newRefreshToken =
                refreshTokenService.createRefreshToken(userId);


        return new LoginResponse(
                userId,
                newAccessToken,
                newRefreshToken
        );
    }
}