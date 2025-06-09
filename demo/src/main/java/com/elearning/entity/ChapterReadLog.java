package com.elearning.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "el_chapter_read_logs")
public class ChapterReadLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "chapter_id", nullable = false)
    private Long chapterId;

    @Column(name = "read_date")
    private LocalDateTime readDate;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @PrePersist
    protected void onCreate() {
        readDate = LocalDateTime.now();
    }

    // Constructors
    public ChapterReadLog() {
    }

    public ChapterReadLog(Long userId, Long chapterId, Boolean isCompleted) {
        this.userId = userId;
        this.chapterId = chapterId;
        this.isCompleted = isCompleted;
    }
}

