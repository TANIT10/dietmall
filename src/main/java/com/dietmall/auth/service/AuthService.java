package com.dietmall.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.auth.dto.LoginRequest;
import com.dietmall.auth.dto.LoginResponse;
import com.dietmall.auth.dto.RefreshTokenRequest;
import com.dietmall.auth.dto.SignupRequest;
import com.dietmall.auth.dto.TokenRefreshResponse;
import com.dietmall.auth.entity.AuthAccount;
import com.dietmall.auth.entity.AuthProvider;
import com.dietmall.auth.exception.DuplicateEmailException;
import com.dietmall.auth.exception.InvalidCredentialsException;
import com.dietmall.auth.jwt.JwtTokenService;
import com.dietmall.auth.repository.AuthAccountRepository;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthAccountRepository authAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserRepository userRepository,
            AuthAccountRepository authAccountRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService,
            RefreshTokenService refreshTokenService) {

        this.userRepository = userRepository;
        this.authAccountRepository = authAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    // 일반 회원가입
    @Transactional
    public Long signup(SignupRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        boolean alreadyExists =
                authAccountRepository.existsByProviderAndProviderUserId(
                        AuthProvider.LOCAL,
                        email
                );

        if (alreadyExists) {
            throw new DuplicateEmailException();
        }

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = new User(request.getNickname());

        User savedUser = userRepository.save(user);

        AuthAccount authAccount = new AuthAccount(
                savedUser,
                AuthProvider.LOCAL,
                email,
                email,
                encodedPassword
        );

        authAccountRepository.save(authAccount);

        return savedUser.getId();
    }

    // 일반 로그인
    @Transactional
    public LoginResponse login(LoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        AuthAccount authAccount =
                authAccountRepository
                        .findByProviderAndProviderUserId(
                                AuthProvider.LOCAL,
                                email
                        )
                        .orElseThrow(InvalidCredentialsException::new);

        String encodedPassword = authAccount.getPassword();

        if (encodedPassword == null ||
                !passwordEncoder.matches(
                        request.getPassword(),
                        encodedPassword
                )) {

            throw new InvalidCredentialsException();
        }

        Long userId = authAccount.getUser().getId();

        String accessToken =
                jwtTokenService.createAccessToken(userId);

        String refreshToken =
                refreshTokenService.createRefreshToken(userId);

        return new LoginResponse(
                userId,
                accessToken,
                refreshToken
        );
    }

    // Access Token + Refresh Token 재발급
    @Transactional
    public TokenRefreshResponse refresh(
            RefreshTokenRequest request) {

        // 1. 기존 Refresh Token 검증
        // 정상이라면 기존 Refresh Token은 DB에서 삭제됨
        Long userId =
                refreshTokenService.verifyAndConsume(
                        request.getRefreshToken()
                );

        // 2. 새로운 Access Token 생성
        String newAccessToken =
                jwtTokenService.createAccessToken(userId);

        // 3. 새로운 Refresh Token 생성 + DB 저장
        String newRefreshToken =
                refreshTokenService.createRefreshToken(userId);

        // 4. 새 토큰들 반환
        return new TokenRefreshResponse(
                userId,
                newAccessToken,
                newRefreshToken
        );
    }
    @Transactional
    public void logout(RefreshTokenRequest request) {
    	
    	refreshTokenService.revoke(
    			request.getRefreshToken()
    			);
    }
}
