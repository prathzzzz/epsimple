package com.eps.module.common.bulk.service;

import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.bulk.excel.ExcelExportUtil;
import com.eps.module.common.bulk.excel.ExcelImportUtil;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class for bulk upload service implementations
 * Provides common functionality for bulk operations
 * 
 * @param <T> The DTO type for bulk upload
 * @param <E> The Entity type
 */
@Slf4j
public abstract class BaseBulkUploadService<T, E> implements BulkUploadService<T, E> {
    
    @Autowired
    protected ExcelImportUtil excelImportUtil;
    
    @Autowired
    protected ExcelExportUtil excelExportUtil;
    
    /**
     * Get the processor for this entity type
     */
    protected abstract BulkUploadProcessor<T, E> getProcessor();
    
    /**
     * Build error report DTO from error with row data
     * Override this to customize error report columns
     */
    protected abstract Object buildErrorReportDto(BulkUploadErrorDto error);
    
    /**
     * Get the class for error report DTO
     */
    protected abstract Class<?> getErrorReportDtoClass();
    
    @Override
    public SseEmitter bulkUpload(MultipartFile file) throws IOException {
        log.info("Starting bulk upload for {}", getEntityName());
        
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            throw new IllegalArgumentException("Invalid file format. Please upload an Excel file (.xlsx)");
        }
        
        // Parse Excel file
        List<T> uploadData = excelImportUtil.parseExcelFile(file, getBulkUploadDtoClass());
        
        log.info("Parsed {} records from Excel file for {}", uploadData.size(), getEntityName());
        
        // Create SSE emitter
        SseEmitter emitter = BulkUploadProcessor.createEmitter();
        
        // Add completion and error handlers
        emitter.onCompletion(() -> log.info("SSE emitter completed successfully for {}", getEntityName()));
        emitter.onTimeout(() -> log.warn("SSE emitter timeout for {}", getEntityName()));
        emitter.onError((ex) -> log.error("SSE emitter error for {}: {}", getEntityName(), ex.getMessage()));
        
        // Process async with virtual threads (processor handles @Async internally)
        getProcessor().processBulkUpload(uploadData, emitter);
        
        log.info("Bulk upload processing started asynchronously for {}", getEntityName());
        
        return emitter;
    }
    
    @Override
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        log.info("Exporting all {} to Excel", getEntityName());
        
        List<E> entities = getAllEntitiesForExport();
        
        List<T> exportData = entities.stream()
                .map(getEntityToDtoMapper())
                .collect(Collectors.toList());
        
        byte[] excelData = excelExportUtil.exportToExcel(
                exportData,
                getBulkUploadDtoClass(),
                getEntityName() + "s"
        );
        
        String filename = getEntityName() + "_Export_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        
        return buildFileResponse(excelData, filename);
    }
    
    @Override
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        log.info("Generating bulk upload template for {}", getEntityName());
        
        byte[] templateData = excelExportUtil.generateTemplate(
                getBulkUploadDtoClass(),
                getEntityName() + "s Template"
        );
        
        String filename = getEntityName() + "_Upload_Template.xlsx";
        
        return buildFileResponse(templateData, filename);
    }
    
    @Override
    public ResponseEntity<byte[]> exportErrorReport(BulkUploadProgressDto progressData) throws IOException {
        log.info("Generating error report for {} bulk upload", getEntityName());
        
        if (progressData.getErrors() == null || progressData.getErrors().isEmpty()) {
            throw new IllegalArgumentException("No errors to export");
        }
        
        // Convert errors to report DTOs using custom builder
        List<Object> errorReports = progressData.getErrors().stream()
                .map(this::buildErrorReportDto)
                .collect(Collectors.toList());
        
        // Use helper method to properly handle generics
        byte[] errorReport = exportErrorReportHelper(errorReports, getErrorReportDtoClass());
        
        String filename = getEntityName() + "_Upload_Errors_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        
        return buildFileResponse(errorReport, filename);
    }
    
    /**
     * Helper method to handle generic type casting for error report export
     * This is needed because we're building a list of error report DTOs dynamically
     */
    @SuppressWarnings("unchecked")
    private <R> byte[] exportErrorReportHelper(List<Object> data, Class<?> clazz) throws IOException {
        return excelExportUtil.exportToExcel((List<R>) data, (Class<R>) clazz, "Upload Errors");
    }
    
    /**
     * Build HTTP response for file download
     */
    protected ResponseEntity<byte[]> buildFileResponse(byte[] data, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(data.length);
        
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}
