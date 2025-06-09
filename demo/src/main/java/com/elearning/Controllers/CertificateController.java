package com.elearning.Controllers;

import com.elearning.DTO.*;
import com.elearning.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/certificate")
@CrossOrigin(origins = "*")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateCertificate(
            @RequestParam Long userId,
            @RequestParam Long chapterId) {
        ApiResponse<Map<String, Object>> response = certificateService.generateCertificate(userId, chapterId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getUserCertificates(@PathVariable Long userId) {
        ApiResponse<List<Map<String, Object>>> response = certificateService.getUserCertificates(userId);
        return ResponseEntity.ok(response);
    }
}

