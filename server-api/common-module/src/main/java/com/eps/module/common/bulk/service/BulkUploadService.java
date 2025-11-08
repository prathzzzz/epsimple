package com.eps.module.common.bulk.service;

import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * Generic interface for bulk upload operations
 * @param <T> The DTO type for bulk upload
 * @param <E> The Entity type
 */
public interface BulkUploadService<T, E> {
    
    /**
     * Handle bulk upload with SSE progress tracking
     */
    SseEmitter bulkUpload(MultipartFile file) throws IOException;
    
    /**
     * Export all entities to Excel
     */
    ResponseEntity<byte[]> exportToExcel() throws IOException;
    
    /**
     * Download template for bulk upload
     */
    ResponseEntity<byte[]> downloadTemplate() throws IOException;
    
    /**
     * Export error report from bulk upload
     */
    ResponseEntity<byte[]> exportErrorReport(BulkUploadProgressDto progressData) throws IOException;
    
    // Entity-specific methods to implement
    
    /**
     * Get the DTO class for bulk upload
     */
    Class<T> getBulkUploadDtoClass();
    
    /**
     * Get the entity name (e.g., "State", "City", "Bank")
     */
    String getEntityName();
    
    /**
     * Get all entities for export
     */
    List<E> getAllEntitiesForExport();
    
    /**
     * Get mapper function from Entity to DTO
     */
    Function<E, T> getEntityToDtoMapper();
}
