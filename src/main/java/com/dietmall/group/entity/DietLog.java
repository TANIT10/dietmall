package com.dietmall.group.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "diet_logs")
public class DietLog {

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
            name = "member_id",
            nullable = false
    )
    private GroupMember author;


    @Column(
            name = "image_url",
            nullable = false,
            length = 500
    )
    private String imageUrl;


    @Column(
            name = "memo",
            length = 500
    )
    private String memo;


    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;


    @Column(
            name = "updated_at",
            nullable = false
    )
    private Instant updatedAt;


    protected DietLog() {
    }


    public DietLog(
            DietGroup group,
            GroupMember author,
            String imageUrl,
            String memo) {

        this.group = group;
        this.author = author;
        this.imageUrl = imageUrl;
        this.memo = memo;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }


    public void update(
            String imageUrl,
            String memo) {

        this.imageUrl = imageUrl;
        this.memo = memo;
        this.updatedAt = Instant.now();
    }


    public void updateMemo(
            String memo) {

        this.memo = memo;
        this.updatedAt = Instant.now();
    }


    public void updateImage(
            String imageUrl) {

        this.imageUrl = imageUrl;
        this.updatedAt = Instant.now();
    }


    public Long getId() {
        return id;
    }


    public DietGroup getGroup() {
        return group;
    }


    public GroupMember getAuthor() {
        return author;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    public String getMemo() {
        return memo;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }


    public Instant getUpdatedAt() {
        return updatedAt;
    }
}