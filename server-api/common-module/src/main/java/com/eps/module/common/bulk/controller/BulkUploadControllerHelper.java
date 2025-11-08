package com.eps.module.common.bulk.controller;

import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * Helper class for bulk upload controller operations
 * Uses composition pattern to provide reusable bulk upload endpoints
 */
@Slf4j
@Component
public class BulkUploadControllerHelper {
    
    /**
     * Handle bulk upload with SSE progress tracking
     */
    public <T, E> SseEmitter bulkUpload(
            MultipartFile file,
            BulkUploadService<T, E> service
    ) throws IOException {
        log.debug("BulkUploadControllerHelper: Processing bulk upload");
        return service.bulkUpload(file);
    }
    
    /**
     * Export all entities to Excel
     */
    public <T, E> ResponseEntity<byte[]> export(
            BulkUploadService<T, E> service
    ) throws IOException {
        log.debug("BulkUploadControllerHelper: Exporting entities");
        return service.exportToExcel();
    }
    
    /**
     * Download bulk upload template
     */
    public <T, E> ResponseEntity<byte[]> downloadTemplate(
            BulkUploadService<T, E> service
    ) throws IOException {
        log.debug("BulkUploadControllerHelper: Downloading template");
        return service.downloadTemplate();
    }
    
    /**
     * Export bulk upload error report
     */
    public <T, E> ResponseEntity<byte[]> exportErrors(
            BulkUploadProgressDto progressData,
            BulkUploadService<T, E> service
    ) throws IOException {
        log.debug("BulkUploadControllerHelper: Exporting error report");
        return service.exportErrorReport(progressData);
    }
}
