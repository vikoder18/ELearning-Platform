package com.elearning.Controllers;

import com.elearning.DTO.QuestionRequestDTO;
import com.elearning.entity.Question;
import com.elearning.repository.QuestionRepository;
import com.elearning.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    QuestionRepository questionRepository;

    @PostMapping("/questions/bulk")
    public ResponseEntity<?> addMultipleQuestions(@RequestBody List<QuestionRequestDTO> questions) {
        List<Question> savedQuestions = questions.stream()
                .map(dto -> new Question(dto.getChapterId(), dto.getQuestionText(),
                        dto.getOptionA(), dto.getOptionB(), dto.getOptionC(),
                        dto.getOptionD(), dto.getCorrectAnswer()))
                .collect(Collectors.toList());

        questionRepository.saveAll(savedQuestions);
        return ResponseEntity.ok("Questions saved successfully");
    }

}

