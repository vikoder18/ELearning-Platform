package com.elearning.service;


import com.elearning.Config.FileStorageConfig;
import com.elearning.utility.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    @Autowired
    private FileStorageConfig fileStorageConfig;

    public String storeFile(MultipartFile file) throws IOException {
        return FileUtil.saveFile(file, fileStorageConfig.getUploadDir());
    }

    public Resource loadFileAsResource(String fileName) throws MalformedURLException {
        Path filePath = Paths.get(fileStorageConfig.getUploadDir()).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("File not found: " + fileName);
        }
    }

    public void deleteFile(String fileName) throws IOException {
        FileUtil.deleteFile(fileName, fileStorageConfig.getUploadDir());
    }
}