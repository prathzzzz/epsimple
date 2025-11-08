package com.eps.module.common.bulk.processor;

import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.bulk.dto.BulkUploadResultDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generic processor for handling bulk uploads with SSE progress tracking
 * Uses Virtual Threads (Java 21+) for efficient concurrent processing
 * @param <T> The DTO type for row data
 * @param <E> The Entity type to persist
 */
@Slf4j
public abstract class BulkUploadProcessor<T, E> {
    
    private static final long SSE_TIMEOUT = 30 * 60 * 1000L; // 30 minutes
    
    /**
     * Process the bulk upload with SSE progress updates
     * Runs asynchronously using virtual threads for better performance
     */
    @Async("taskExecutor")
    public void processBulkUpload(List<T> rowDataList, SseEmitter emitter) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();
        AtomicInteger processedCount = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        AtomicInteger duplicateCount = new AtomicInteger(0);
        AtomicInteger skippedCount = new AtomicInteger(0);
        
        int totalRecords = rowDataList.size();
        
        try {
            // Send initial progress
            sendProgress(emitter, "PROCESSING", totalRecords, 0, 0, 0, 0, 0, "Starting bulk upload...", null);
            
            BulkRowValidator<T> validator = getValidator();
            
            for (int i = 0; i < rowDataList.size(); i++) {
                T rowData = rowDataList.get(i);
                int rowNumber = i + 2; // +2 because row 1 is header and arrays are 0-indexed
                
                try {
                    // Validate the row
                    List<BulkUploadErrorDto> rowErrors = validator.validate(rowData, rowNumber);
                    
                    if (!rowErrors.isEmpty()) {
                        // Mark all errors with VALIDATION type and add row data
                        rowErrors.forEach(error -> {
                            error.setErrorType("VALIDATION");
                            error.setRowData(getRowDataAsMap(rowData));
                        });
                        errors.addAll(rowErrors);
                        failureCount.incrementAndGet();
                        skippedCount.incrementAndGet();
                    } else if (validator.isDuplicate(rowData)) {
                        errors.add(BulkUploadErrorDto.builder()
                                .rowNumber(rowNumber)
                                .errorMessage("Duplicate record found")
                                .errorType("DUPLICATE")
                                .rowData(getRowDataAsMap(rowData))
                                .build());
                        duplicateCount.incrementAndGet();
                        skippedCount.incrementAndGet();
                    } else {
                        // Convert DTO to Entity and save
                        E entity = convertToEntity(rowData);
                        saveEntity(entity);
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    log.error("Error processing row {}: {}", rowNumber, e.getMessage(), e);
                    errors.add(BulkUploadErrorDto.builder()
                            .rowNumber(rowNumber)
                            .errorMessage("Error: " + e.getMessage())
                            .errorType("ERROR")
                            .rowData(getRowDataAsMap(rowData))
                            .build());
                    failureCount.incrementAndGet();
                    skippedCount.incrementAndGet();
                }
                
                processedCount.incrementAndGet();
                
                // Send progress update every 10 records or on last record
                if (processedCount.get() % 10 == 0 || processedCount.get() == totalRecords) {
                    double progress = (processedCount.get() * 100.0) / totalRecords;
                    sendProgress(emitter, "PROCESSING", totalRecords, processedCount.get(), 
                            successCount.get(), failureCount.get(), duplicateCount.get(), skippedCount.get(),
                            String.format("Processing... %d/%d records", processedCount.get(), totalRecords),
                            null);
                }
            }
            
            // Send final result
            String status = failureCount.get() > 0 || duplicateCount.get() > 0 ? "COMPLETED_WITH_ERRORS" : "COMPLETED";
            String message = String.format("Upload completed. Success: %d, Failed: %d, Duplicates: %d, Skipped: %d", 
                    successCount.get(), failureCount.get(), duplicateCount.get(), skippedCount.get());
            
            sendProgress(emitter, status, totalRecords, processedCount.get(), 
                    successCount.get(), failureCount.get(), duplicateCount.get(), skippedCount.get(), message, errors);
            
            emitter.complete();
            
        } catch (Exception e) {
            log.error("Fatal error during bulk upload: {}", e.getMessage(), e);
            try {
                sendProgress(emitter, "FAILED", totalRecords, processedCount.get(), 
                        successCount.get(), failureCount.get(), duplicateCount.get(), skippedCount.get(),
                        "Upload failed: " + e.getMessage(), errors);
                emitter.completeWithError(e);
            } catch (IOException ioException) {
                log.error("Error sending failure notification: {}", ioException.getMessage());
            }
        }
    }
    
    /**
     * Send progress update via SSE
     */
    private void sendProgress(SseEmitter emitter, String status, int total, int processed, 
                              int success, int failure, int duplicate, int skipped, String message, 
                              List<BulkUploadErrorDto> errors) throws IOException {
        double progress = total > 0 ? (processed * 100.0) / total : 0;
        
        BulkUploadProgressDto progressDto = BulkUploadProgressDto.builder()
                .status(status)
                .totalRecords(total)
                .processedRecords(processed)
                .successCount(success)
                .failureCount(failure)
                .duplicateCount(duplicate)
                .skippedCount(skipped)
                .progressPercentage(progress)
                .message(message)
                .errors(errors)
                .build();
        
        emitter.send(SseEmitter.event()
                .name("progress")
                .data(progressDto));
    }
    
    /**
     * Get the validator for this entity type
     */
    protected abstract BulkRowValidator<T> getValidator();
    
    /**
     * Convert DTO to Entity
     */
    protected abstract E convertToEntity(T dto);
    
    /**
     * Save the entity to database
     */
    protected abstract void saveEntity(E entity);
    
    /**
     * Convert row data to Map for error reporting
     */
    protected abstract java.util.Map<String, Object> getRowDataAsMap(T dto);
    
    /**
     * Create SSE Emitter with timeout
     */
    public static SseEmitter createEmitter() {
        return new SseEmitter(SSE_TIMEOUT);
    }
}
