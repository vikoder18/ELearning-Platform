package com.elearning.DTO;

import lombok.Data;

@Data
public class QuestionRequestDTO {
    private Long chapterId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
}
