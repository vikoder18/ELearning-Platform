package com.elearning.Controllers;

import com.elearning.DTO.ApiResponse;
import com.elearning.DTO.TestAttemptStatusDTO;
import com.elearning.DTO.TestResultResponseDTO;
import com.elearning.DTO.TestSubmissionDTO;
import com.elearning.entity.TestSession;
import com.elearning.service.TestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<Map<String, Object>>> startTest(
            @RequestParam Long userId,
            @RequestParam(required = false) Long chapterId // now optional
            ) {
        ApiResponse<Map<String, Object>> response = testService.startTest(chapterId, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<TestResultResponseDTO>> submitTest(
            @Valid @RequestBody TestSubmissionDTO submission,
            @RequestParam Long userId) {
        ApiResponse<TestResultResponseDTO> response = testService.submitTest(submission, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/result/{testSessionId}")
    public ResponseEntity<ApiResponse<TestResultResponseDTO>> getTestResult(
            @PathVariable String testSessionId,
            @RequestParam Long userId) {
        ApiResponse<TestResultResponseDTO> response = testService.getTestResult(testSessionId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/attempts")
    public ResponseEntity<TestAttemptStatusDTO> getUserAttemptStatus(
            @RequestParam Long userId) {
        return ResponseEntity.ok(testService.getAttemptStatus(userId));
    }


    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<TestSession>>> getUserTestHistory(@RequestParam Long userId) {
        ApiResponse<List<TestSession>> response = testService.getUserTestHistory(userId);
        return ResponseEntity.ok(response);
    }
}

