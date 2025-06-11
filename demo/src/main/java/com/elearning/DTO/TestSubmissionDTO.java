package com.elearning.DTO;
import lombok.Data;
import java.util.*;

@Data
public class TestSubmissionDTO {
    private Long testSessionId;
    private List<AnswerDTO> answers;

    public static class AnswerDTO {
        private Long questionId;
        private String selectedAnswer;

        // Constructors
        public AnswerDTO() {}

        public AnswerDTO(Long questionId, String selectedAnswer) {
            this.questionId = questionId;
            this.selectedAnswer = selectedAnswer;
        }

        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }

        public String getSelectedAnswer() { return selectedAnswer; }
        public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer;}


    }

    // Constructors
    public TestSubmissionDTO() {}

    public TestSubmissionDTO(Long testSessionId, List<AnswerDTO> answers) {
        this.testSessionId = testSessionId;
        this.answers = answers;
    }
}