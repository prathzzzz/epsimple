package com.eps.module.api.epsone.site_activity_work_expenditure.controller;

import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureRequestDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureResponseDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.service.SiteActivityWorkExpenditureService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/site-activity-work-expenditures")
@RequiredArgsConstructor
public class SiteActivityWorkExpenditureController {

    private final SiteActivityWorkExpenditureService service;

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
