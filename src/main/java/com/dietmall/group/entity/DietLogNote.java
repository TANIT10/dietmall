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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "diet_log_notes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_diet_log_note_member",
                        columnNames = {
                                "log_id",
                                "member_id"
                        }
                )
        }
)
public class DietLogNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "log_id",
            nullable = false
    )
    private DietLog log;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    private GroupMember author;


    @Column(
            nullable = false,
            length = 20
    )
    private String content;


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


    protected DietLogNote() {
    }


    public DietLogNote(
            DietLog log,
            GroupMember author,
            String content) {

        this.log = log;
        this.author = author;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }


    public void updateContent(
            String content) {

        this.content = content;
        this.updatedAt = Instant.now();
    }


    public Long getId() {
        return id;
    }


    public DietLog getLog() {
        return log;
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


    public Instant getUpdatedAt() {
        return updatedAt;
    }
}