package com.elearning.service;

import com.elearning.DTO.*;
import com.elearning.entity.*;
import com.elearning.repository.*;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

    public byte[] generateCertificatePdf(Long userId) throws Exception {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        String username = userOpt.get().getUsername();

        // Load template
        InputStream templateStream = new ClassPathResource("templates/Final_certificate_template.pdf").getInputStream();
        PdfReader reader = new PdfReader(templateStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        Document document = new Document(pdfDoc);

        // Inject user name on the first page
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        float fontSize = 120;

        PdfPage page = pdfDoc.getFirstPage();
        Rectangle pageSize = page.getPageSize();

// Calculate X (centered)
        float textWidth = font.getWidth(username, fontSize);
        float x = (pageSize.getWidth() - textWidth) / 2;

// Y: just above the blank line
        float y = 950; // Adjust between 370–380 as needed for alignment

        canvas.beginText()
                .setFontAndSize(font, fontSize)
                .moveText(x, y)
                .showText(username)
                .endText();

        pdfDoc.close();
        return baos.toByteArray();
    }

    public ApiResponse<Map<String, Object>> generateCertificate(Long userId) {
        try {
            // Check if user has passed the test for this chapter
            Optional<TestSession> sessionOpt = testSessionRepository
                    .findByUserIdAndPassed(userId, true);

            if (sessionOpt.isEmpty()) {
                return ApiResponse.error("No passing test found for this chapter", null);
            }

            TestSession session = sessionOpt.get();

            // Get user and chapter details
            Optional<User> userOpt = userRepository.findById(userId);
           // Optional<Chapter> chapterOpt = chapterRepository.findById(chapterId);

//            if (userOpt.isEmpty() || chapterOpt.isEmpty()) {
//                return ApiResponse.error("User or chapter not found", null);
//            }

            User user = userOpt.get();
           // Chapter chapter = chapterOpt.get();

            // Create certificate data
            Map<String, Object> certificateData = new HashMap<>();
            certificateData.put("userName", user.getUsername());
           // certificateData.put("chapterTitle", chapter.getTitle());
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