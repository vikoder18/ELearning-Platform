package com.elearning.service;

import com.elearning.DTO.QuestionRequestDTO;
import com.elearning.entity.Question;
import com.elearning.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question addQuestion(QuestionRequestDTO dto) {
        Question question = new Question(
                dto.getChapterId(),
                dto.getQuestionText(),
                dto.getOptionA(),
                dto.getOptionB(),
                dto.getOptionC(),
                dto.getOptionD(),
                dto.getCorrectAnswer()
        );
        return questionRepository.save(question);
    }
}
