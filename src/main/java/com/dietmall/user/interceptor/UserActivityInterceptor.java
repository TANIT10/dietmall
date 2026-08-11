package com.dietmall.user.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.dietmall.user.service.UserActivityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserActivityInterceptor
        implements HandlerInterceptor {

    private final UserActivityService userActivityService;


    public UserActivityInterceptor(
            UserActivityService userActivityService) {

        this.userActivityService =
                userActivityService;
    }


    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        if (authentication == null
                || !authentication.isAuthenticated()) {

            return true;
        }


        Object principal =
                authentication.getPrincipal();


        if (!(principal instanceof Jwt jwt)) {

            return true;
        }


        String subject =
                jwt.getSubject();


        if (subject == null || subject.isBlank()) {

            return true;
        }


        Long userId =
                Long.valueOf(subject);


        userActivityService.touch(userId);


        return true;
    }
}