package com.elearning.repository;

import com.elearning.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByTestSessionId(String testSessionId);
    List<TestResult> findByUserIdAndChapterId(Long userId, Long chapterId);
    List<TestResult> findByUserIdAndChapterIdAndAttemptNumber(Long userId, Long chapterId, Integer attemptNumber);
}
