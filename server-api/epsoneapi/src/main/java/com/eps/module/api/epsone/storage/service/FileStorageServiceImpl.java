package com.eps.module.api.epsone.storage.service;

import com.eps.module.api.epsone.storage.dto.FileUploadResponseDto;
import com.eps.module.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload.base-dir:uploads}")
    private String baseUploadDir;

    @Value("${app.base-url:}")
    private String appBaseUrl;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/png", "image/jpeg", "image/jpg", "image/svg+xml", "image/webp"
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public FileUploadResponseDto uploadFile(MultipartFile file, String directory) {
        validateFile(file);

        try {
            // Create directory if it doesn't exist
            Path uploadPath = Paths.get(baseUploadDir, directory);
            Files.createDirectories(uploadPath);

            // Generate unique filename
            String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;

            // Copy file to target location
            Path targetLocation = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Generate file URL
            String fileUrl;
            if (appBaseUrl != null && !appBaseUrl.isEmpty()) {
                // Use configured base URL for production
                fileUrl = appBaseUrl + "/api/files/" + directory + "/" + uniqueFilename;
            } else {
                // Fallback to ServletUriComponentsBuilder for development
                fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/")
                    .path(directory)
                    .path("/")
                    .path(uniqueFilename)
                    .toUriString();
            }

            log.info("File uploaded successfully: {}", uniqueFilename);

            return FileUploadResponseDto.builder()
                .fileName(uniqueFilename)
                .fileUrl(fileUrl)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();

        } catch (IOException ex) {
            log.error("Failed to upload file: {}", file.getOriginalFilename(), ex);
            throw new CustomException("Failed to upload file: " + ex.getMessage());
        }
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(baseUploadDir, filePath);
            Files.deleteIfExists(path);
            log.info("File deleted successfully: {}", filePath);
        } catch (IOException ex) {
            log.error("Failed to delete file: {}", filePath, ex);
            throw new CustomException("Failed to delete file: " + ex.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String filePath) {
        try {
            Path path = Paths.get(baseUploadDir, filePath);
            if (!Files.exists(path)) {
                throw new CustomException("File not found: " + filePath);
            }
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            log.error("Failed to download file: {}", filePath, ex);
            throw new CustomException("Failed to download file: " + ex.getMessage());
        }
    }

    @Override
    public boolean fileExists(String filePath) {
        Path path = Paths.get(baseUploadDir, filePath);
        return Files.exists(path);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CustomException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CustomException("File size exceeds maximum limit of 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new CustomException("Invalid file type. Allowed types: PNG, JPEG, JPG, SVG, WEBP");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || filename.contains("..")) {
            throw new CustomException("Invalid filename");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
