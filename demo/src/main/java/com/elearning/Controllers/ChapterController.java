package com.elearning.Controllers;

import com.elearning.DTO.ApiResponse;
import com.elearning.DTO.ChapterResponseDTO;
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


    @GetMapping
    public ResponseEntity<ApiResponse<List<ChapterResponseDTO>>> getAllChapters(@RequestParam Long userId) {
        ApiResponse<List<ChapterResponseDTO>> response = chapterService.getAllChapters(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{chapterId}")
    public ResponseEntity<ApiResponse<ChapterResponseDTO>> getChapterById(
            @PathVariable Long chapterId,
            @RequestParam Long userId) {
        ApiResponse<ChapterResponseDTO> response = chapterService.getChapterById(chapterId, userId);
        return ResponseEntity.ok(response);
    }
}
