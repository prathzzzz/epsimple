package com.eps.module.api.epsone.site_activity_work_expenditure.controller;

import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureBulkUploadDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureRequestDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureResponseDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.service.SiteActivityWorkExpenditureService;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import com.eps.module.site.SiteActivityWorkExpenditure;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/site-activity-work-expenditures")
@RequiredArgsConstructor
public class SiteActivityWorkExpenditureController {

    private final SiteActivityWorkExpenditureService service;
    private final BulkUploadControllerHelper bulkUploadHelper;

    // ============== Bulk Upload Endpoints ==============

    @PostMapping(value = "/bulk-upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("POST /api/site-activity-work-expenditures/bulk-upload - Starting bulk upload");
        return bulkUploadHelper.bulkUpload(file, service);
    }

    @GetMapping("/bulk-upload/template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        log.info("GET /api/site-activity-work-expenditures/bulk-upload/template - Downloading template");
        return bulkUploadHelper.downloadTemplate(service);
    }

    @GetMapping("/bulk-upload/export")
    public ResponseEntity<byte[]> export() throws Exception {
        log.info("GET /api/site-activity-work-expenditures/bulk-upload/export - Exporting all data");
        return bulkUploadHelper.export(service);
    }

    @PostMapping("/bulk-upload/errors")
    public ResponseEntity<byte[]> exportErrors(@RequestBody BulkUploadProgressDto progressData) throws Exception {
        log.info("POST /api/site-activity-work-expenditures/bulk-upload/errors - Exporting error report");
        return bulkUploadHelper.exportErrors(progressData, service);
    }

    // ============== Regular CRUD Endpoints ==============


    @PostMapping
    public ResponseEntity<ApiResponse<SiteActivityWorkExpenditureResponseDto>> createSiteActivityWorkExpenditure(
            @Valid @RequestBody SiteActivityWorkExpenditureRequestDto requestDto) {
        
        log.info("POST /api/site-activity-work-expenditures - Creating site activity work expenditure");
        SiteActivityWorkExpenditureResponseDto responseDto = service.createSiteActivityWorkExpenditure(requestDto);
        return ResponseBuilder.success(responseDto, "Site activity work expenditure created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SiteActivityWorkExpenditureResponseDto>>> getAllSiteActivityWorkExpenditures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        
        log.info("GET /api/site-activity-work-expenditures - Fetching all expenditures");
        Page<SiteActivityWorkExpenditureResponseDto> expenditures = 
                service.getAllSiteActivityWorkExpenditures(page, size, sortBy, sortOrder);
        return ResponseBuilder.success(expenditures, "Site activity work expenditures fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<ApiResponse<Page<SiteActivityWorkExpenditureResponseDto>>> getExpendituresBySiteId(
            @PathVariable Long siteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        
        log.info("GET /api/site-activity-work-expenditures/site/{} - Fetching expenditures for site", siteId);
        Page<SiteActivityWorkExpenditureResponseDto> expenditures = 
                service.getExpendituresBySiteId(siteId, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(expenditures, "Site expenditures fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/activity-work/{activityWorkId}")
    public ResponseEntity<ApiResponse<Page<SiteActivityWorkExpenditureResponseDto>>> getExpendituresByActivityWorkId(
            @PathVariable Long activityWorkId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        
        log.info("GET /api/site-activity-work-expenditures/activity-work/{} - Fetching expenditures for activity work", activityWorkId);
        Page<SiteActivityWorkExpenditureResponseDto> expenditures = 
                service.getExpendituresByActivityWorkId(activityWorkId, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(expenditures, "Activity work expenditures fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SiteActivityWorkExpenditureResponseDto>>> searchExpenditures(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        
        log.info("GET /api/site-activity-work-expenditures/search - Searching expenditures with keyword: {}", keyword);
        Page<SiteActivityWorkExpenditureResponseDto> expenditures = 
                service.searchExpenditures(keyword, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(expenditures, "Site activity work expenditures search completed", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteActivityWorkExpenditureResponseDto>> getExpenditureById(
            @PathVariable Long id) {
        
        log.info("GET /api/site-activity-work-expenditures/{} - Fetching expenditure", id);
        SiteActivityWorkExpenditureResponseDto responseDto = service.getExpenditureById(id);
        return ResponseBuilder.success(responseDto, "Site activity work expenditure fetched successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteActivityWorkExpenditureResponseDto>> updateSiteActivityWorkExpenditure(
            @PathVariable Long id,
            @Valid @RequestBody SiteActivityWorkExpenditureRequestDto requestDto) {
        
        log.info("PUT /api/site-activity-work-expenditures/{} - Updating expenditure", id);
        SiteActivityWorkExpenditureResponseDto responseDto = service.updateSiteActivityWorkExpenditure(id, requestDto);
        return ResponseBuilder.success(responseDto, "Site activity work expenditure updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSiteActivityWorkExpenditure(
            @PathVariable Long id) {
        
        log.info("DELETE /api/site-activity-work-expenditures/{} - Deleting expenditure", id);
        service.deleteSiteActivityWorkExpenditure(id);
        return ResponseBuilder.success(null, "Site activity work expenditure deleted successfully", HttpStatus.OK);
    }
}
