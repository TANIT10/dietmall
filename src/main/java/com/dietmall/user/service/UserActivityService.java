package com.dietmall.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.user.entity.User;
import com.dietmall.user.entity.UserActivity;
import com.dietmall.user.repository.UserActivityRepository;
import com.dietmall.user.repository.UserRepository;

@Service
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;


    public UserActivityService(
            UserActivityRepository userActivityRepository,
            UserRepository userRepository) {

        this.userActivityRepository =
                userActivityRepository;

        this.userRepository =
                userRepository;
    }


    @Transactional
    public void touch(Long userId) {

        UserActivity activity =
                userActivityRepository
                        .findByUserId(userId)
                        .orElse(null);


        if (activity != null) {

            activity.touch();

            return;
        }


        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "사용자를 찾을 수 없습니다."
                                )
                        );


        UserActivity newActivity =
                new UserActivity(user);


        userActivityRepository.save(newActivity);
    }
}