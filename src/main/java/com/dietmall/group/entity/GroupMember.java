package com.dietmall.group.entity;

import java.time.Instant;

import com.dietmall.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
        name = "group_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_group_member_user",
                        columnNames = {
                                "group_id",
                                "user_id"
                        }
                ),
                @UniqueConstraint(
                        name = "uk_group_member_nickname",
                        columnNames = {
                                "group_id",
                                "group_nickname"
                        }
                )
        }
)
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "group_id",
            nullable = false
    )
    private DietGroup group;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;


    @Column(
            name = "group_nickname",
            nullable = false,
            length = 30
    )
    private String groupNickname;


    @Column(
            name = "profile_image_url",
            length = 500
    )
    private String profileImageUrl;


    @Column(
            name = "joined_at",
            nullable = false
    )
    private Instant joinedAt;


    protected GroupMember() {
    }


    public GroupMember(
            DietGroup group,
            User user,
            String groupNickname,
            String profileImageUrl) {

        this.group = group;
        this.user = user;
        this.groupNickname = groupNickname;
        this.profileImageUrl = profileImageUrl;
        this.joinedAt = Instant.now();
    }


    public void updateProfile(
            String groupNickname,
            String profileImageUrl) {

        this.groupNickname = groupNickname;
        this.profileImageUrl = profileImageUrl;
    }


    public void updateNickname(
            String groupNickname) {

        this.groupNickname = groupNickname;
    }


    public void updateProfileImage(
            String profileImageUrl) {

        this.profileImageUrl = profileImageUrl;
    }


    public void removeProfileImage() {

        this.profileImageUrl = null;
    }


    public Long getId() {
        return id;
    }


    public DietGroup getGroup() {
        return group;
    }


    public User getUser() {
        return user;
    }


    public String getGroupNickname() {
        return groupNickname;
    }


    public String getProfileImageUrl() {
        return profileImageUrl;
    }


    public Instant getJoinedAt() {
        return joinedAt;
    }
}