package com.elearning.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "el_test_results")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "chapter_id", nullable = false)
    private Long chapterId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "selected_answer", length = 1)
    private String selectedAnswer;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "test_session_id")
    private String testSessionId;

    @Column(name = "attempt_number")
    private Integer attemptNumber = 1;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }

    // Constructors
    public TestResult() {
    }

    public TestResult(Long userId, Long chapterId, Long questionId, String selectedAnswer,
                      Boolean isCorrect, String testSessionId, Integer attemptNumber) {
        this.userId = userId;
        this.chapterId = chapterId;
        this.questionId = questionId;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
        this.testSessionId = testSessionId;
        this.attemptNumber = attemptNumber;
    }
}
