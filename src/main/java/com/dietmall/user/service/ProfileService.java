package com.dietmall.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.user.dto.UserProfileResponse;
import com.dietmall.user.dto.UserProfileUpdateRequest;
import com.dietmall.user.entity.UserProfile;
import com.dietmall.user.repository.UserProfileRepository;

@Service
public class ProfileService {

    private final UserProfileRepository userProfileRepository;

    public ProfileService(
            UserProfileRepository userProfileRepository) {

        this.userProfileRepository = userProfileRepository;
    }

    // 내 프로필 조회
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId) {

        UserProfile userProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자 프로필을 찾을 수 없습니다."
                        )
                );

        return UserProfileResponse.from(userProfile);
    }

    // 내 프로필 수정
    @Transactional
    public UserProfileResponse updateProfile(
            Long userId,
            UserProfileUpdateRequest request) {

        UserProfile userProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자 프로필을 찾을 수 없습니다."
                        )
                );

        userProfile.updateProfile(
                request.getExerciseLevel(),
                request.getDietDifficulty(),
                request.getAlcoholFrequency(),
                request.getMealPreference()
        );

        return UserProfileResponse.from(userProfile);
    }
}