package com.dietmall.user.entity;

import java.math.BigDecimal;

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
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 사용자의 목표인지 연결
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    // 목표 체중
    @Column(
            name = "target_weight",
            nullable = false,
            precision = 5,
            scale = 2
    )
    private BigDecimal targetWeight;

    protected Goal() {
    }

    public Goal(
            User user,
            BigDecimal targetWeight) {

        this.user = user;
        this.targetWeight = targetWeight;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getTargetWeight() {
        return targetWeight;
    }
}