package com.elearning.repository;

import com.elearning.entity.ChapterReadLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterReadLogRepository extends JpaRepository<ChapterReadLog, Long> {
    Optional<ChapterReadLog> findByUserIdAndChapterId(Long userId, Long chapterId);
    List<ChapterReadLog> findByUserIdAndIsCompleted(Long userId, Boolean isCompleted);
    List<ChapterReadLog> findByUserId(Long userId);
    long countByUserIdAndIsCompleted(Long userId, boolean isCompleted);
    boolean existsByUserIdAndChapterIdAndIsCompleted(Long userId, Long chapterId, Boolean isCompleted);
}
