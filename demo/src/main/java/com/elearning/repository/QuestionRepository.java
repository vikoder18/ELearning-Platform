package com.elearning.repository;

import com.elearning.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByChapterId(Long chapterId);

    @Query(value = "SELECT * FROM el_questions WHERE chapter_id = :chapterId ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestionsByChapterId(@Param("chapterId") Long chapterId, @Param("limit") int limit);

    @Query(value = "SELECT DISTINCT chapter_id FROM el_questions", nativeQuery = true)
    List<Long> findAllDistinctChapterIds();

    @Query(value = "SELECT * FROM el_questions WHERE chapter_id = :chapterId ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Question> findOneRandomQuestionByChapterId(@Param("chapterId") Long chapterId);

    @Query(value = "SELECT * FROM el_questions WHERE id NOT IN (:excludeIds) ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestionsExcludingIds(@Param("excludeIds") List<Long> excludeIds, @Param("limit") int limit);

    Long countByChapterId(Long chapterId);
}

