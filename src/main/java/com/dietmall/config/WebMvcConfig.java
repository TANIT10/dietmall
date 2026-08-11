package com.dietmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dietmall.user.interceptor.UserActivityInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserActivityInterceptor userActivityInterceptor;


    public WebMvcConfig(
            UserActivityInterceptor userActivityInterceptor) {

        this.userActivityInterceptor =
                userActivityInterceptor;
    }


    @Override
    public void addInterceptors(
            InterceptorRegistry registry) {

        registry.addInterceptor(userActivityInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/signup",
                        "/api/auth/login",
                        "/api/auth/refresh",
                        "/api/auth/logout",
                        "/api/auth/kakao/login",
                        "/api/auth/google/login",
                        "/api/health"
                );
    }
}