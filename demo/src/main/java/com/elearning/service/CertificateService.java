package com.elearning.service;

import com.elearning.DTO.*;
import com.elearning.entity.*;
import com.elearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CertificateService {

    @Autowired
    private TestSessionRepository testSessionRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private UserRepository userRepository;

    public ApiResponse<Map<String, Object>> generateCertificate(Long userId, Long chapterId) {
        try {
            // Check if user has passed the test for this chapter
            Optional<TestSession> sessionOpt = testSessionRepository
                    .findByUserIdAndChapterIdAndPassed(userId, chapterId, true);

            if (sessionOpt.isEmpty()) {
                return ApiResponse.error("No passing test found for this chapter", null);
            }

            TestSession session = sessionOpt.get();

            // Get user and chapter details
            Optional<User> userOpt = userRepository.findById(userId);
            Optional<Chapter> chapterOpt = chapterRepository.findById(chapterId);

            if (userOpt.isEmpty() || chapterOpt.isEmpty()) {
                return ApiResponse.error("User or chapter not found", null);
            }

            User user = userOpt.get();
            Chapter chapter = chapterOpt.get();

            // Create certificate data
            Map<String, Object> certificateData = new HashMap<>();
            certificateData.put("userName", user.getUsername());
            certificateData.put("chapterTitle", chapter.getTitle());
            certificateData.put("scorePercentage", session.getScorePercentage());
            certificateData.put("completedDate", session.getCompletedAt());
            certificateData.put("certificateId", "CERT-" + session.getId());

            // In production, you would generate a PDF here
            // For now, we'll return the certificate data

            return ApiResponse.success("Certificate generated successfully", certificateData);

        } catch (Exception e) {
            return ApiResponse.error("Failed to generate certificate: " + e.getMessage(), null);
        }
    }

    public ApiResponse<List<Map<String, Object>>> getUserCertificates(Long userId) {
        try {
            List<TestSession> passedSessions = testSessionRepository
                    .findByUserIdOrderByStartedAtDesc(userId)
                    .stream()
                    .filter(TestSession::getPassed)
                    .collect(Collectors.toList());

            List<Map<String, Object>> certificates = new ArrayList<>();

            for (TestSession session : passedSessions) {
                Optional<Chapter> chapterOpt = chapterRepository.findById(session.getChapterId());
                if (chapterOpt.isPresent()) {
                    Chapter chapter = chapterOpt.get();
                    Map<String, Object> certificate = new HashMap<>();
                    certificate.put("certificateId", "CERT-" + session.getId());
                    certificate.put("chapterTitle", chapter.getTitle());
                    certificate.put("scorePercentage", session.getScorePercentage());
                    certificate.put("completedDate", session.getCompletedAt());

                    certificates.add(certificate);
                }
            }

            return ApiResponse.success("User certificates retrieved successfully", certificates);

        } catch (Exception e) {
            return ApiResponse.error("Failed to retrieve certificates: " + e.getMessage(), null);
        }
    }
}