package com.dietmall.user.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_activities")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;


    @Column(
            name = "last_active_at",
            nullable = false
    )
    private Instant lastActiveAt;


    protected UserActivity() {
    }


    public UserActivity(User user) {
        this.user = user;
        this.lastActiveAt = Instant.now();
    }


    public void touch() {
        this.lastActiveAt = Instant.now();
    }


    public Long getId() {
        return id;
    }


    public User getUser() {
        return user;
    }


    public Instant getLastActiveAt() {
        return lastActiveAt;
    }
}