package com.elearning.service;

import com.elearning.DTO.*;
import com.elearning.entity.*;
import com.elearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TestService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ChapterReadLogRepository chapterReadLogRepository;

    @Autowired
    private TestSessionRepository testSessionRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Transactional
    public ApiResponse<Map<String, Object>> startTest(@Nullable Long chapterId, Long userId) {

        int totalQuestions = 10;

        // Step 1: Get distinct chapter IDs
        List<Long> chapterIds = questionRepository.findAllDistinctChapterIds();

        // If chapter count > totalQuestions, limit chapters
        if (chapterIds.size() > totalQuestions) {
            chapterIds = chapterIds.subList(0, totalQuestions);
        }

        // Step 2: One random question from each chapter
        List<Question> mandatoryQuestions = new ArrayList<>();
        for (Long cid : chapterIds) {
            questionRepository.findOneRandomQuestionByChapterId(cid)
                    .ifPresent(mandatoryQuestions::add);
        }

        // Step 3: Calculate remaining questions needed
        int remaining = totalQuestions - mandatoryQuestions.size();

        List<Long> usedQuestionIds = mandatoryQuestions.stream()
                .map(Question::getId)
                .collect(Collectors.toList());

        // Step 4: Fetch additional random questions (exclude already used)
        List<Question> remainingQuestions = new ArrayList<>();
        if (remaining > 0) {
            remainingQuestions = questionRepository.findRandomQuestionsExcludingIds(usedQuestionIds, remaining);
        }

        // Step 5: Combine & shuffle
        List<Question> finalQuestionSet = new ArrayList<>(mandatoryQuestions);
        finalQuestionSet.addAll(remainingQuestions);
        Collections.shuffle(finalQuestionSet);

        //Step 5: Create TestSession and save
        TestSession session = new TestSession();
        session.setUserId(userId);
        if (chapterId != null) {
            session.setChapterId(chapterId);
        }
        //session.setChapterId(chapterId); // Optional
        session.setStartedAt(LocalDateTime.now());
        //session.setQuestions(finalQuestionSet); // Save question list if supported
        testSessionRepository.save(session);

        //Step 6: Return session ID in response
        Map<String, Object> data = new HashMap<>();
        data.put("testSessionId", session.getId());
        data.put("questions", finalQuestionSet);

        return new ApiResponse<>(true, "Test started", data);
    }



    public ApiResponse<TestResultResponseDTO> submitTest(TestSubmissionDTO submission, Long userId) {
        try {
            // Get test session
            Optional<TestSession> sessionOpt = testSessionRepository.findById(String.valueOf(submission.getTestSessionId()));
            if (sessionOpt.isEmpty()) {
                return ApiResponse.error("Invalid test session", null);
            }

            TestSession session = sessionOpt.get();

            // Verify user owns this session
            if (!session.getUserId().equals(userId)) {
                return ApiResponse.error("Unauthorized access to test session", null);
            }

            List<TestResultResponseDTO.QuestionResultDTO> questionResults = new ArrayList<>();
            int correctAnswers = 0;

            // Process each answer
            for (TestSubmissionDTO.AnswerDTO answer : submission.getAnswers()) {
                Optional<Question> questionOpt = questionRepository.findById(Long.valueOf(answer.getQuestionId()));
                if (questionOpt.isPresent()) {
                    Question question = questionOpt.get();
                    boolean isCorrect = question.getCorrectAnswer().equals(answer.getSelectedAnswer());

                    if (isCorrect) {
                        correctAnswers++;
                    }

                    // Save test result
                    TestResult result = new TestResult(
                            userId,
                            session.getChapterId(),
                            question.getId(),
                            answer.getSelectedAnswer(),
                            isCorrect,
                            session.getId(),
                            session.getAttemptNumber()
                    );
                    testResultRepository.save(result);

                    // Get chapter reference for wrong answers
                    String chapterReference = null;
                    if (!isCorrect) {
                        Optional<Chapter> chapterOpt = chapterRepository.findById(question.getChapterId());
                        chapterReference = chapterOpt.map(Chapter::getTitle).orElse("Chapter " + question.getChapterId());
                    }

                    // Add to question results
                    TestResultResponseDTO.QuestionResultDTO questionResult =
                            new TestResultResponseDTO.QuestionResultDTO(
                                    question.getId(),
                                    question.getQuestionText(),
                                    answer.getSelectedAnswer(),
                                    question.getCorrectAnswer(),
                                    isCorrect,
                                    chapterReference
                            );
                    questionResults.add(questionResult);
                }
            }

            // Calculate score
            double scorePercentage = (double) correctAnswers / session.getTotalQuestions() * 100;
            boolean passed = scorePercentage >= 75.0;

            // Update test session
            session.setCorrectAnswers(correctAnswers);
            session.setScorePercentage(BigDecimal.valueOf(scorePercentage));
            session.setPassed(passed);
            session.setCompletedAt(LocalDateTime.now());

            if (passed) {
                session.setCertificateGenerated(true);
            }

            testSessionRepository.save(session);

            // Create response
            TestResultResponseDTO response = new TestResultResponseDTO();
            response.setTestSessionId(session.getId());
            response.setChapterId(session.getChapterId());

            Optional<Chapter> chapterOpt = chapterRepository.findById(session.getChapterId());
            response.setChapterTitle(chapterOpt.map(Chapter::getTitle).orElse("Chapter " + session.getChapterId()));

            response.setTotalQuestions(session.getTotalQuestions());
            response.setCorrectAnswers(correctAnswers);
            response.setScorePercentage(scorePercentage);
            response.setPassed(passed);
            response.setCertificateGenerated(passed);
            response.setQuestionResults(questionResults);

            return ApiResponse.success("Test submitted successfully", response);

        } catch (Exception e) {
            return ApiResponse.error("Failed to submit test: " + e.getMessage(), null);
        }
    }

    public ApiResponse<TestResultResponseDTO> getTestResult(String testSessionId, Long userId) {
        try {
            Optional<TestSession> sessionOpt = testSessionRepository.findById(testSessionId);
            if (sessionOpt.isEmpty()) {
                return ApiResponse.error("Test session not found", null);
            }

            TestSession session = sessionOpt.get();

            // Verify user owns this session
            if (!session.getUserId().equals(userId)) {
                return ApiResponse.error("Unauthorized access to test results", null);
            }

            // Get test results
            List<TestResult> results = testResultRepository.findByTestSessionId(testSessionId);

            List<TestResultResponseDTO.QuestionResultDTO> questionResults = new ArrayList<>();

            for (TestResult result : results) {
                Optional<Question> questionOpt = questionRepository.findById(result.getQuestionId());
                if (questionOpt.isPresent()) {
                    Question question = questionOpt.get();

                    String chapterReference = null;
                    if (!result.getIsCorrect()) {
                        Optional<Chapter> chapterOpt = chapterRepository.findById(question.getChapterId());
                        chapterReference = chapterOpt.map(Chapter::getTitle).orElse("Chapter " + question.getChapterId());
                    }

                    TestResultResponseDTO.QuestionResultDTO questionResult =
                            new TestResultResponseDTO.QuestionResultDTO(
                                    question.getId(),
                                    question.getQuestionText(),
                                    result.getSelectedAnswer(),
                                    question.getCorrectAnswer(),
                                    result.getIsCorrect(),
                                    chapterReference
                            );
                    questionResults.add(questionResult);
                }
            }

            // Create response
            TestResultResponseDTO response = new TestResultResponseDTO();
            response.setTestSessionId(session.getId());
            response.setChapterId(session.getChapterId());

            Optional<Chapter> chapterOpt = chapterRepository.findById(session.getChapterId());
            response.setChapterTitle(chapterOpt.map(Chapter::getTitle).orElse("Chapter " + session.getChapterId()));

            response.setTotalQuestions(session.getTotalQuestions());
            response.setCorrectAnswers(session.getCorrectAnswers());
            response.setScorePercentage(session.getScorePercentage().doubleValue());
            response.setPassed(session.getPassed());
            response.setCertificateGenerated(session.getCertificateGenerated());
            response.setQuestionResults(questionResults);

            return ApiResponse.success("Test results retrieved successfully", response);

        } catch (Exception e) {
            return ApiResponse.error("Failed to retrieve test results: " + e.getMessage(), null);
        }
    }

    public ApiResponse<List<TestSession>> getUserTestHistory(Long userId) {
        try {
            List<TestSession> sessions = testSessionRepository.findByUserIdOrderByStartedAtDesc(userId);
            return ApiResponse.success("Test history retrieved successfully", sessions);
        } catch (Exception e) {
            return ApiResponse.error("Failed to retrieve test history: " + e.getMessage(), null);
        }
    }
}
