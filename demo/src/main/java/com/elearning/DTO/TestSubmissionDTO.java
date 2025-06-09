package com.elearning.DTO;
import lombok.Data;
import java.util.*;

@Data
public class TestSubmissionDTO {
    private String testSessionId;
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

        // Getters and Setters
        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }

        public String getSelectedAnswer() { return selectedAnswer; }
        public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }
    }

    // Constructors
    public TestSubmissionDTO() {}

    public TestSubmissionDTO(String testSessionId, List<AnswerDTO> answers) {
        this.testSessionId = testSessionId;
        this.answers = answers;
    }

    // Getters and Setters
    public String getTestSessionId() { return testSessionId; }
    public void setTestSessionId(String testSessionId) { this.testSessionId = testSessionId; }

    public List<AnswerDTO> getAnswers() { return answers; }
    public void setAnswers(List<AnswerDTO> answers) { this.answers = answers; }
}