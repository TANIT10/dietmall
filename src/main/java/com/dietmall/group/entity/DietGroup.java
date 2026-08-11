package com.dietmall.group.entity;

import java.time.Instant;

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

@Entity
@Table(name = "diet_groups")
public class DietGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 50)
    private String name;


    @Column(length = 200)
    private String description;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GroupVisibility visibility;


    @Column(
            name = "invite_code",
            nullable = false,
            unique = true,
            length = 20
    )
    private String inviteCode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "owner_user_id",
            nullable = false
    )
    private User owner;


    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;


    protected DietGroup() {
    }


    public DietGroup(
            String name,
            String description,
            GroupVisibility visibility,
            String inviteCode,
            User owner) {

        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.inviteCode = inviteCode;
        this.owner = owner;
        this.createdAt = Instant.now();
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public GroupVisibility getVisibility() {
        return visibility;
    }


    public String getInviteCode() {
        return inviteCode;
    }


    public User getOwner() {
        return owner;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }
}