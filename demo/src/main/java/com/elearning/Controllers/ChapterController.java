package com.elearning.Controllers;

import com.elearning.DTO.ApiResponse;
import com.elearning.DTO.ChapterResponseDTO;
import com.elearning.DTO.ChapterVisitDTO;
import com.elearning.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@CrossOrigin(origins = "*")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @PostMapping("/create-chapter")
    public ResponseEntity<ApiResponse<ChapterResponseDTO>> createChapter(@RequestBody ChapterResponseDTO chapterResponse) {
        ApiResponse<ChapterResponseDTO> response = chapterService.createChapter(chapterResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/content-urls")
    public ResponseEntity<ApiResponse<List<String>>> getAllContentUrls() {
        ApiResponse<List<String>> response = chapterService.getAllContentUrls();
        return ResponseEntity.ok(response);
    }

    // GET all chapters with user-specific visit status
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ChapterResponseDTO>>> getAllChaptersForUser(@PathVariable Long userId) {
        ApiResponse<List<ChapterResponseDTO>> response = chapterService.getAllChaptersForUser(userId);
        return ResponseEntity.ok(response);
    }

    // POST to mark chapter as visited (called from frontend when user visits a chapter)
    @PostMapping("/visit")
    public ResponseEntity<ApiResponse<String>> markChapterAsVisited(@RequestBody ChapterVisitDTO visitDTO) {
        ApiResponse<String> response = chapterService.markChapterAsVisited(visitDTO.getUserId(), visitDTO.getChapterId());
        return ResponseEntity.ok(response);
    }

    // GET to check if user can start test (has visited all chapters)
    @GetMapping("/test-eligibility/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> canUserStartTest(@PathVariable Long userId) {
        ApiResponse<Boolean> response = chapterService.canUserStartTest(userId);
        return ResponseEntity.ok(response);
    }

    // GET specific chapter details (without side effects)
    @GetMapping("/{chapterId}")
    public ResponseEntity<ApiResponse<ChapterResponseDTO>> getChapterById(@PathVariable Long chapterId) {
        ApiResponse<ChapterResponseDTO> response = chapterService.getChapterById(chapterId);
        return ResponseEntity.ok(response);
    }
}