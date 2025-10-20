package com.eps.module.api.epsone.storage.controller;

import com.eps.module.api.epsone.storage.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/**")
    public ResponseEntity<Resource> serveFile(HttpServletRequest request) {
        // Extract the path after /api/files/
        String requestURL = request.getRequestURI();
        String filePath = requestURL.substring(requestURL.indexOf("/api/files/") + "/api/files/".length());
        
        log.info("Serving file: {}", filePath);

        try {
            byte[] fileData = fileStorageService.downloadFile(filePath);
            ByteArrayResource resource = new ByteArrayResource(fileData);

            // Determine content type based on file extension
            String contentType = determineContentType(filePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + extractFileName(filePath) + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error serving file: {}", filePath, e);
            return ResponseEntity.notFound().build();
        }
    }

    private String extractFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf('/') + 1);
    }

    private String determineContentType(String filename) {
        String lowerCaseFilename = filename.toLowerCase();
        if (lowerCaseFilename.endsWith(".png")) {
            return "image/png";
        } else if (lowerCaseFilename.endsWith(".jpg") || lowerCaseFilename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerCaseFilename.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (lowerCaseFilename.endsWith(".webp")) {
            return "image/webp";
        } else if (lowerCaseFilename.endsWith(".gif")) {
            return "image/gif";
        }
        return "application/octet-stream";
    }
}
