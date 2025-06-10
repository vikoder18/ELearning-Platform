package com.elearning.service;

import com.elearning.DTO.*;
import com.elearning.entity.*;
import com.elearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
@Transactional
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private ChapterReadLogRepository chapterReadLogRepository;

    @Autowired
    private TestSessionRepository testSessionRepository;

    // Get all chapters with user-specific visit status
    public ApiResponse<List<ChapterResponseDTO>> getAllChaptersForUser(Long userId) {
        try {
            List<Chapter> chapters = chapterRepository.findAllByOrderByCreatedAtAsc();
            List<ChapterResponseDTO> chapterResponses = new ArrayList<>();

            // Check if user can start test (has visited all chapters)
            boolean canStartTest = hasVisitedAllChapters(userId);

            for (Chapter chapter : chapters) {
                // Check if this specific chapter has been visited by user
                boolean isVisited = chapterReadLogRepository.existsByUserIdAndChapterIdAndIsCompleted(
                        userId, chapter.getId(), true
                );

                ChapterResponseDTO response = new ChapterResponseDTO(
                        chapter.getId(),
                        chapter.getTitle(),
                        chapter.getDescription(),
                        chapter.getContentType().toString(),
                        chapter.getContentUrl(),
                        isVisited,      // individual chapter visit status
                        canStartTest    // global test eligibility (same for all chapters)
                );

                chapterResponses.add(response);
            }

            return ApiResponse.success("Chapters retrieved successfully", chapterResponses);

        } catch (Exception e) {
            return ApiResponse.error("Failed to retrieve chapters: " + e.getMessage(), null);
        }
    }

    // Mark chapter as visited (called from frontend when user accesses a chapter)
    public ApiResponse<String> markChapterAsVisited(Long userId, Long chapterId) {
        try {
            // Check if chapter exists
            if (!chapterRepository.existsById(chapterId)) {
                return ApiResponse.error("Chapter not found", null);
            }

            // Check if already visited
            Optional<ChapterReadLog> existingLog = chapterReadLogRepository
                    .findByUserIdAndChapterId(userId, chapterId);

            if (existingLog.isPresent() && existingLog.get().getIsCompleted()) {
                return ApiResponse.success("Chapter already marked as visited", "Already visited");
            }

            // Create or update visit log
            ChapterReadLog readLog = existingLog.orElse(new ChapterReadLog());
            readLog.setUserId(userId);
            readLog.setChapterId(chapterId);
            readLog.setIsCompleted(true);
            readLog.setVisitedAt(LocalDateTime.now()); // Add timestamp if needed

            chapterReadLogRepository.save(readLog);

            return ApiResponse.success("Chapter marked as visited successfully", "Visited");

        } catch (Exception e) {
            return ApiResponse.error("Failed to mark chapter as visited: " + e.getMessage(), null);
        }
    }

    // Check if user can start test (has visited all chapters)
    public ApiResponse<Boolean> canUserStartTest(Long userId) {
        try {
            boolean canStart = hasVisitedAllChapters(userId);
            String message = canStart ?
                    "User can start the test" :
                    "User must visit all chapters before starting the test";

            return ApiResponse.success(message, canStart);
        } catch (Exception e) {
            return ApiResponse.error("Failed to check test eligibility: " + e.getMessage(), false);
        }
    }

    // Helper method to check if user has visited all chapters
    private boolean hasVisitedAllChapters(Long userId) {
        long totalChapters = chapterRepository.count();
        if (totalChapters == 0) return false; // Edge case: no chapters exist

        long visitedChapters = chapterReadLogRepository.countByUserIdAndIsCompleted(userId, true);
        return visitedChapters >= totalChapters;
    }

    // Get chapter by ID without side effects (pure read operation)
    public ApiResponse<ChapterResponseDTO> getChapterById(Long chapterId) {
        try {
            Optional<Chapter> chapterOpt = chapterRepository.findById(chapterId);
            if (chapterOpt.isEmpty()) {
                return ApiResponse.error("Chapter not found", null);
            }

            Chapter chapter = chapterOpt.get();

            ChapterResponseDTO response = new ChapterResponseDTO(
                    chapter.getId(),
                    chapter.getTitle(),
                    chapter.getDescription(),
                    chapter.getContentType().toString(),
                    chapter.getContentUrl(),
                    false,  // Visit status not relevant here
                    false   // Test eligibility not relevant here
            );

            return ApiResponse.success("Chapter retrieved successfully", response);

        } catch (Exception e) {
            return ApiResponse.error("Failed to retrieve chapter: " + e.getMessage(), null);
        }
    }

    // Existing methods remain the same
    public ApiResponse<List<String>> getAllContentUrls() {
        try {
            List<Chapter> chapters = chapterRepository.findAll();
            List<String> contentUrls = chapters.stream()
                    .map(Chapter::getContentUrl)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return ApiResponse.success("Fetched all content URLs", contentUrls);
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch content URLs: " + e.getMessage(), null);
        }
    }

    public ApiResponse<ChapterResponseDTO> createChapter(ChapterResponseDTO dto) {
        try {
            // Map DTO to Entity
            Chapter chapter = new Chapter();
            chapter.setTitle(dto.getTitle());
            chapter.setDescription(dto.getDescription());
            chapter.setContentType(Chapter.ContentType.valueOf(dto.getContentType().toUpperCase()));
            chapter.setContentUrl(dto.getContentUrl());

            // Save to DB
            Chapter savedChapter = chapterRepository.save(chapter);

            // Map back to DTO
            ChapterResponseDTO responseDTO = new ChapterResponseDTO(
                    savedChapter.getId(),
                    savedChapter.getTitle(),
                    savedChapter.getDescription(),
                    savedChapter.getContentType().name(),
                    savedChapter.getContentUrl(),
                    false, // New chapter, not visited
                    false  // Test eligibility depends on all chapters
            );

            return ApiResponse.success("Chapter created successfully", responseDTO);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create chapter: " + e.getMessage(), null);
        }
    }
}