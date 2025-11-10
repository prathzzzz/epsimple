package com.eps.module.api.epsone.site_category.controller;

import com.eps.module.api.epsone.site_category.dto.SiteCategoryBulkUploadDto;
import com.eps.module.api.epsone.site_category.dto.SiteCategoryRequestDto;
import com.eps.module.api.epsone.site_category.dto.SiteCategoryResponseDto;
import com.eps.module.api.epsone.site_category.service.SiteCategoryService;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import com.eps.module.site.SiteCategory;
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
@RequestMapping("/api/site-categories")
@RequiredArgsConstructor
public class SiteCategoryController {

    private final SiteCategoryService siteCategoryService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    @PostMapping
    public ResponseEntity<ApiResponse<SiteCategoryResponseDto>> createSiteCategory(@Valid @RequestBody SiteCategoryRequestDto requestDto) {
        log.info("POST /api/site-categories - Creating new site category");
        SiteCategoryResponseDto response = siteCategoryService.createSiteCategory(requestDto);
        return ResponseBuilder.success(response, "Site category created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SiteCategoryResponseDto>>> getAllSiteCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        log.info("GET /api/site-categories - Fetching all site categories with pagination");
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SiteCategoryResponseDto> siteCategories = siteCategoryService.getAllSiteCategories(pageable);
        return ResponseBuilder.success(siteCategories, "Site categories retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SiteCategoryResponseDto>>> searchSiteCategories(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        log.info("GET /api/site-categories/search - Searching site categories with term: {}", searchTerm);
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SiteCategoryResponseDto> siteCategories = siteCategoryService.searchSiteCategories(searchTerm, pageable);
        return ResponseBuilder.success(siteCategories, "Site categories search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<SiteCategoryResponseDto>>> getSiteCategoryList() {
        log.info("GET /api/site-categories/list - Fetching all site categories as list");
        List<SiteCategoryResponseDto> siteCategories = siteCategoryService.getSiteCategoryList();
        return ResponseBuilder.success(siteCategories, "Site categories list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteCategoryResponseDto>> getSiteCategoryById(@PathVariable Long id) {
        log.info("GET /api/site-categories/{} - Fetching site category by ID", id);
        SiteCategoryResponseDto siteCategory = siteCategoryService.getSiteCategoryById(id);
        return ResponseBuilder.success(siteCategory, "Site category retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteCategoryResponseDto>> updateSiteCategory(
            @PathVariable Long id,
            @Valid @RequestBody SiteCategoryRequestDto requestDto) {
        log.info("PUT /api/site-categories/{} - Updating site category", id);
        SiteCategoryResponseDto updatedSiteCategory = siteCategoryService.updateSiteCategory(id, requestDto);
        return ResponseBuilder.success(updatedSiteCategory, "Site category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSiteCategory(@PathVariable Long id) {
        log.info("DELETE /api/site-categories/{} - Deleting site category", id);
        siteCategoryService.deleteSiteCategory(id);
        return ResponseBuilder.success(null, "Site category deleted successfully");
    }

    // ============ Bulk Upload Endpoints ============

    @PostMapping(value = "/bulk-upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter bulkUploadSiteCategories(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("POST /api/site-categories/bulk-upload - Starting bulk upload for site categories");
        return bulkUploadControllerHelper.bulkUpload(file, siteCategoryService);
    }

    @GetMapping("/bulk-upload/template")
    public ResponseEntity<byte[]> downloadBulkUploadTemplate() throws Exception {
        log.info("GET /api/site-categories/bulk-upload/template - Downloading bulk upload template");
        return bulkUploadControllerHelper.downloadTemplate(siteCategoryService);
    }

    @GetMapping("/bulk-upload/export")
    public ResponseEntity<byte[]> exportSiteCategories() throws Exception {
        log.info("GET /api/site-categories/bulk-upload/export - Exporting all site categories");
        return bulkUploadControllerHelper.export(siteCategoryService);
    }

    @PostMapping("/bulk-upload/errors")
    public ResponseEntity<byte[]> downloadErrorReport(@RequestBody com.eps.module.common.bulk.dto.BulkUploadProgressDto progressData) throws Exception {
        log.info("POST /api/site-categories/bulk-upload/errors - Downloading error report");
        return bulkUploadControllerHelper.exportErrors(progressData, siteCategoryService);
    }
}
