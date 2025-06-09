package com.elearning.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CertificateUtil {

    public static String generateCertificateContent(String userName, String courseName, double score) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String currentDate = LocalDateTime.now().format(formatter);

        return String.format(
                "CERTIFICATE OF COMPLETION\n\n" +
                        "This is to certify that\n\n" +
                        "%s\n\n" +
                        "has successfully completed the course\n\n" +
                        "%s\n\n" +
                        "with a score of %.2f%%\n\n" +
                        "Date: %s\n\n" +
                        "E-Learning Platform",
                userName, courseName, score, currentDate
        );
    }

    public static String generateCertificateId(Long userId, Long chapterId) {
        return String.format("CERT-%d-%d-%d",
                userId, chapterId, System.currentTimeMillis());
    }
}
