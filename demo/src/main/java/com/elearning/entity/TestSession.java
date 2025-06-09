package com.elearning.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "el_test_sessions")
public class TestSession {
    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "chapter_id", nullable = false)
    private Long chapterId;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "correct_answers")
    private Integer correctAnswers;

    @Column(name = "score_percentage", precision = 5, scale = 2)
    private BigDecimal scorePercentage;

    @Column(name = "passed")
    private Boolean passed = false;

    @Column(name = "certificate_generated")
    private Boolean certificateGenerated = false;

    @Column(name = "attempt_number")
    private Integer attemptNumber = 1;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        startedAt = LocalDateTime.now();
    }

    // Constructors
    public TestSession() {
    }

    public TestSession(String id, Long userId, Long chapterId, Integer totalQuestions) {
        this.id = id;
        this.userId = userId;
        this.chapterId = chapterId;
        this.totalQuestions = totalQuestions;
    }
}
