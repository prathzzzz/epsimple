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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
                    // Check if this is an empty row first
                    boolean isEmptyRow = isEmptyRow(rowData);
                    
                    if (isEmptyRow) {
                        // Skip empty rows silently - don't count as anything
                        processedCount.incrementAndGet();
                        continue;
                    }
                    
                    // Validate the row
                    List<BulkUploadErrorDto> rowErrors = validator.validate(rowData, rowNumber);
                    
                    if (!rowErrors.isEmpty()) {
                        // Combine all validation errors for this row into a single error message
                        String combinedMessage = rowErrors.stream()
                                .map(BulkUploadErrorDto::getErrorMessage)
                                .collect(Collectors.joining("; "));
                        
                        // Create a single error entry for all validation errors
                        BulkUploadErrorDto combinedError = BulkUploadErrorDto.builder()
                                .rowNumber(rowNumber)
                                .errorMessage(combinedMessage)
                                .errorType("VALIDATION")
                                .rowData(getRowDataAsMap(rowData))
                                .build();
                        
                        errors.add(combinedError);
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
            
            log.info("Bulk upload completed - Status: {}, Success: {}, Failed: {}, Duplicates: {}, Skipped: {}, Errors count: {}", 
                    status, successCount.get(), failureCount.get(), duplicateCount.get(), skippedCount.get(), errors.size());
            
            sendProgress(emitter, status, totalRecords, processedCount.get(), 
                    successCount.get(), failureCount.get(), duplicateCount.get(), skippedCount.get(), message, errors);
            
            log.info("Final SSE event sent, completing emitter");
            emitter.complete();
            log.info("Emitter completed successfully");
            
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
        
        log.debug("Sending SSE progress: status={}, processed={}/{}, success={}, failed={}, duplicates={}, errors={}", 
                status, processed, total, success, failure, duplicate, errors != null ? errors.size() : 0);
        
        emitter.send(SseEmitter.event()
                .name("progress")
                .data(progressDto));
        
        log.debug("SSE progress sent successfully");
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
     * Check if the row is empty (all required fields are null or empty)
     * Override this method in subclasses to define what constitutes an empty row
     */
    protected abstract boolean isEmptyRow(T dto);
    
    /**
     * Create SSE Emitter with timeout
     */
    public static SseEmitter createEmitter() {
        return new SseEmitter(SSE_TIMEOUT);
    }
}
