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
@Table(name = "group_chat_messages")
public class GroupChatMessage {

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
            nullable = false,
            length = 500
    )
    private String content;


    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;


    protected GroupChatMessage() {
    }


    public GroupChatMessage(
            DietGroup group,
            GroupMember author,
            String content) {

        this.group = group;
        this.author = author;
        this.content = content;
        this.createdAt = Instant.now();
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


    public String getContent() {
        return content;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }
}