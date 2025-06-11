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

}