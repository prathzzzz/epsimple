package com.eps.module.api.epsone.site_type.controller;

import com.eps.module.api.epsone.site_type.dto.SiteTypeRequestDto;
import com.eps.module.api.epsone.site_type.dto.SiteTypeResponseDto;
import com.eps.module.api.epsone.site_type.service.SiteTypeService;
import com.eps.module.auth.rbac.annotation.RequireAdmin;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/site-types")
@RequiredArgsConstructor
@RequireAdmin
public class SiteTypeController {

    private final SiteTypeService siteTypeService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    @PostMapping
    public ResponseEntity<ApiResponse<SiteTypeResponseDto>> createSiteType(@Valid @RequestBody SiteTypeRequestDto requestDto) {
        log.info("POST /api/site-types - Creating new site type");
        SiteTypeResponseDto response = siteTypeService.createSiteType(requestDto);
        return ResponseBuilder.success(response, "Site type created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SiteTypeResponseDto>>> getAllSiteTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/site-types - Fetching all site types with pagination");
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SiteTypeResponseDto> siteTypes = siteTypeService.getAllSiteTypes(pageable);
        return ResponseBuilder.success(siteTypes, "Site types retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SiteTypeResponseDto>>> searchSiteTypes(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/site-types/search - Searching site types with term: {}", searchTerm);
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SiteTypeResponseDto> siteTypes = siteTypeService.searchSiteTypes(searchTerm, pageable);
        return ResponseBuilder.success(siteTypes, "Site types search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<SiteTypeResponseDto>>> getSiteTypeList() {
        log.info("GET /api/site-types/list - Fetching all site types as list");
        List<SiteTypeResponseDto> siteTypes = siteTypeService.getSiteTypeList();
        return ResponseBuilder.success(siteTypes, "Site types list retrieved successfully");
    }

    // ========== Export Endpoint (must be before /{id}) ==========

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() throws Exception {
        log.info("GET /api/site-types/export - Exporting all site types");
        return bulkUploadControllerHelper.export(siteTypeService);
    }

    // ========== CRUD Endpoints ==========

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteTypeResponseDto>> getSiteTypeById(@PathVariable Long id) {
        log.info("GET /api/site-types/{} - Fetching site type by ID", id);
        SiteTypeResponseDto siteType = siteTypeService.getSiteTypeById(id);
        return ResponseBuilder.success(siteType, "Site type retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteTypeResponseDto>> updateSiteType(
            @PathVariable Long id,
            @Valid @RequestBody SiteTypeRequestDto requestDto) {
        log.info("PUT /api/site-types/{} - Updating site type", id);
        SiteTypeResponseDto updatedSiteType = siteTypeService.updateSiteType(id, requestDto);
        return ResponseBuilder.success(updatedSiteType, "Site type updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSiteType(@PathVariable Long id) {
        log.info("DELETE /api/site-types/{} - Deleting site type", id);
        siteTypeService.deleteSiteType(id);
        return ResponseBuilder.success(null, "Site type deleted successfully");
    }

    // ============ Bulk Upload Endpoints ============

    @PostMapping(value = "/bulk-upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter bulkUploadSiteTypes(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("POST /api/site-types/bulk-upload - Starting bulk upload for site types");
        return bulkUploadControllerHelper.bulkUpload(file, siteTypeService);
    }

    @GetMapping("/bulk-upload/template")
    public ResponseEntity<byte[]> downloadBulkUploadTemplate() throws Exception {
        log.info("GET /api/site-types/bulk-upload/template - Downloading bulk upload template");
        return bulkUploadControllerHelper.downloadTemplate(siteTypeService);
    }

    @PostMapping("/bulk-upload/errors")
    public ResponseEntity<byte[]> downloadErrorReport(@RequestBody com.eps.module.common.bulk.dto.BulkUploadProgressDto progressData) throws Exception {
        log.info("POST /api/site-types/bulk-upload/errors - Downloading error report");
        return bulkUploadControllerHelper.exportErrors(progressData, siteTypeService);
    }
}
