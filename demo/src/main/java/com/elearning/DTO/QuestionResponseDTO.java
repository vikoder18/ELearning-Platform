package com.elearning.DTO;
import lombok.Data;
@Data
public class QuestionResponseDTO {
    private Long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    // Note: correctAnswer is not included for security

    // Constructors
    public QuestionResponseDTO() {
    }

    public QuestionResponseDTO(Long id, String questionText, String optionA, String optionB,
                               String optionC, String optionD) {
        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }
}
