package com.dietmall.auth.entity;

import com.dietmall.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "auth_accounts",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_auth_provider_user",
            columnNames = {"provider", "provider_user_id"}
        )
    }
)
public class AuthAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProvider provider;

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;

    @Column
    private String email;

    @Column
    private String password;

    protected AuthAccount() {
    }

    public AuthAccount(
            User user,
            AuthProvider provider,
            String providerUserId,
            String email,
            String password) {

        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}