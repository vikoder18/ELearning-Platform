package com.elearning.DTO;
import java.util.*;
import lombok.Data;

@Data
public class TestResultResponseDTO {
    private Long testSessionId;
    private Long chapterId;
    private String chapterTitle;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Double scorePercentage;
    private Boolean passed;
    private Boolean certificateGenerated;
    private List<QuestionResultDTO> questionResults;

    public static class QuestionResultDTO {
        private Long questionId;
        private String questionText;
        private String selectedAnswer;
        private String correctAnswer;
        private Boolean isCorrect;
        private String chapterReference;

        // Constructors
        public QuestionResultDTO() {
        }

        public QuestionResultDTO(Long questionId, String questionText, String selectedAnswer,
                                 String correctAnswer, Boolean isCorrect, String chapterReference) {
            this.questionId = questionId;
            this.questionText = questionText;
            this.selectedAnswer = selectedAnswer;
            this.correctAnswer = correctAnswer;
            this.isCorrect = isCorrect;
            this.chapterReference = chapterReference;
        }
    }
}