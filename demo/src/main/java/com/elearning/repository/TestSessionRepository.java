package com.elearning.repository;

import com.elearning.entity.TestSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestSessionRepository extends JpaRepository<TestSession, String> {
    List<TestSession> findByUserIdAndChapterId(Long userId, Long chapterId);
    List<TestSession> findByUserIdOrderByStartedAtDesc(Long userId);

    @Query("SELECT COUNT(ts) FROM TestSession ts WHERE ts.userId = :userId AND ts.chapterId = :chapterId")
    Integer countAttemptsByUserIdAndChapterId(@Param("userId") Long userId, @Param("chapterId") Long chapterId);

    Optional<TestSession> findByUserIdAndChapterIdAndPassed(Long userId, Long chapterId, Boolean passed);
}
