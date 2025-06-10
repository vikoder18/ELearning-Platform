package com.elearning.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "el_chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private ContentType contentType;

    @Column(name = "content_url", length = 500)
    private String contentUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_read")
    private boolean read;

    @Column(name = "can_start_test")
    private boolean canStartTest;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ContentType {
        PDF, CHAPTER
    }

    // Constructors
    public Chapter() {
    }

    public Chapter(String title, String description, ContentType contentType, String contentUrl) {
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.contentUrl = contentUrl;
    }
}
