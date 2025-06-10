package com.elearning.entity;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "chapter_read_log")
public class ChapterReadLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "chapter_id", nullable = false)
    private Long chapterId;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "visited_at")
    private LocalDateTime visitedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor
    public ChapterReadLog() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with parameters
    public ChapterReadLog(Long userId, Long chapterId, Boolean isCompleted) {
        this.userId = userId;
        this.chapterId = chapterId;
        this.isCompleted = isCompleted;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (isCompleted) {
            this.visitedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ChapterReadLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", chapterId=" + chapterId +
                ", isCompleted=" + isCompleted +
                ", visitedAt=" + visitedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}