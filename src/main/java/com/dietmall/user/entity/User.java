package com.dietmall.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String nickname;

    @Column(nullable = false)
    private boolean onboardingCompleted = false;

    protected User() {
    }

    public User(String nickname) {
        this.nickname = nickname;
        this.onboardingCompleted = false;
    }

    // 온보딩 완료 처리
    public void completeOnboarding() {
        this.onboardingCompleted = true;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isOnboardingCompleted() {
        return onboardingCompleted;
    }
}