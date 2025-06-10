package com.elearning.service;

import com.elearning.DTO.*;
import com.elearning.entity.*;
import com.elearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private ChapterReadLogRepository chapterReadLogRepository;

    @Autowired
    private TestSessionRepository testSessionRepository;

    public ApiResponse<List<ChapterResponseDTO>> getAllChapters(Long userId) {
        try {
            List<Chapter> chapters = chapterRepository.findAllByOrderByCreatedAtAsc();
            List<ChapterResponseDTO> chapterResponses = new ArrayList<>();

            for (Chapter chapter : chapters) {
                // Check if chapter is read
                boolean isRead = chapterReadLogRepository.existsByUserIdAndChapterIdAndIsCompleted(
                        userId, chapter.getId(), true
                );

                // Check if user can start test (chapter must be read)
                boolean canStartTest = isRead;

                ChapterResponseDTO response = new ChapterResponseDTO(
                        chapter.getId(),
                        chapter.getTitle(),
                        chapter.getDescription(),
                        chapter.getContentType().toString(),
                        chapter.getContentUrl(),
                        isRead,
                        canStartTest
                );

                chapterResponses.add(response);
            }

            return ApiResponse.success("Chapters retrieved successfully", chapterResponses);

        } catch (Exception e) {
            return ApiResponse.error("Failed to retrieve chapters: " + e.getMessage(), null);
        }
    }

    public ApiResponse<ChapterResponseDTO> createChapter(ChapterResponseDTO dto) {
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
                savedChapter.isRead(),
                savedChapter.isCanStartTest()
        );

        return ApiResponse.success("Chapter created successfully", responseDTO);
    }



    public ApiResponse<ChapterResponseDTO> getChapterById(Long chapterId, Long userId) {
        try {
            Optional<Chapter> chapterOpt = chapterRepository.findById(chapterId);
            if (chapterOpt.isEmpty()) {
                return ApiResponse.error("Chapter not found", null);
            }

            Chapter chapter = chapterOpt.get();

            // Mark chapter as read
            ChapterReadLog readLog = chapterReadLogRepository
                    .findByUserIdAndChapterId(userId, chapterId)
                    .orElse(new ChapterReadLog(userId, chapterId, true));

            readLog.setIsCompleted(true);
            chapterReadLogRepository.save(readLog);

            ChapterResponseDTO response = new ChapterResponseDTO(
                    chapter.getId(),
                    chapter.getTitle(),
                    chapter.getDescription(),
                    chapter.getContentType().toString(),
                    chapter.getContentUrl(),
                    true,
                    true
            );

            return ApiResponse.success("Chapter retrieved successfully", response);

        } catch (Exception e) {
            return ApiResponse.error("Failed to retrieve chapter: " + e.getMessage(), null);
        }
    }
}

