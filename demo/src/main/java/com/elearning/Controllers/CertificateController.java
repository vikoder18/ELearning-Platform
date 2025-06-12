package com.elearning.Controllers;

import com.elearning.DTO.*;
import com.elearning.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/certificate")
@CrossOrigin(origins = "*")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;



//    @PostMapping("/generate")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> generateCertificate(
//            @RequestParam Long userId) {
//        ApiResponse<Map<String, Object>> response = certificateService.generateCertificate(userId);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateCertificate(@RequestParam Long userId) {
        try {
            byte[] pdfBytes = certificateService.generateCertificatePdf(userId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate_" + userId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getUserCertificates(@PathVariable Long userId) {
        ApiResponse<List<Map<String, Object>>> response = certificateService.getUserCertificates(userId);
        return ResponseEntity.ok(response);
    }
}

