package com.elearning.DTO;
import lombok.Data;
@Data
public class ChapterResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String contentType;
    private String contentUrl;
    private boolean isRead;
    private boolean canStartTest;

    // Constructors
    public ChapterResponseDTO() {
    }

    public ChapterResponseDTO(Long id, String title, String description, String contentType,
                              String contentUrl, boolean isRead, boolean canStartTest) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.isRead = isRead;
        this.canStartTest = canStartTest;
    }
}