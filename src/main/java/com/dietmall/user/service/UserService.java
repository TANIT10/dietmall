package com.dietmall.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.user.dto.UserMeResponse;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserMeResponse getMyInfo(Long userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        )
                );

        return new UserMeResponse(
                user.getId(),
                user.getNickname(),
                user.isOnboardingCompleted()
        );
    }
}