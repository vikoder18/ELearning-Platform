package com.elearning.DTO;

public class ChapterVisitDTO {
    private Long userId;
    private Long chapterId;

    // Default constructor
    public ChapterVisitDTO() {}

    // Constructor with parameters
    public ChapterVisitDTO(Long userId, Long chapterId) {
        this.userId = userId;
        this.chapterId = chapterId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    @Override
    public String toString() {
        return "ChapterVisitDTO{" +
                "userId=" + userId +
                ", chapterId=" + chapterId +
                '}';
    }
}
