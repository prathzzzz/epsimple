package com.eps.module.api.epsone.vendor_category.controller;

import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryRequestDto;
import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryResponseDto;
import com.eps.module.api.epsone.vendor_category.service.VendorCategoryService;
import com.eps.module.auth.rbac.annotation.RequireAdmin;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
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
@RequestMapping("/api/vendor-categories")
@RequiredArgsConstructor
public class VendorCategoryController {

    private final VendorCategoryService vendorCategoryService;
    private final BulkUploadControllerHelper bulkUploadHelper;

    @PostMapping
    @RequireAdmin
    public ResponseEntity<ApiResponse<VendorCategoryResponseDto>> createVendorCategory(
            @Valid @RequestBody VendorCategoryRequestDto requestDto) {
        log.info("POST /api/vendor-categories - Creating new vendor category");
        VendorCategoryResponseDto createdVendorCategory = vendorCategoryService.createVendorCategory(requestDto);
        return ResponseBuilder.success(createdVendorCategory, "Vendor category created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<VendorCategoryResponseDto>>> getAllVendorCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/vendor-categories - Fetching all vendor categories with pagination");
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<VendorCategoryResponseDto> vendorCategories = vendorCategoryService.getAllVendorCategories(pageable);
        
        return ResponseBuilder.success(vendorCategories, "Vendor categories retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<VendorCategoryResponseDto>>> searchVendorCategories(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/vendor-categories/search - Searching vendor categories with keyword: {}", query);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<VendorCategoryResponseDto> vendorCategories = vendorCategoryService.searchVendorCategories(query, pageable);
        
        return ResponseBuilder.success(vendorCategories, "Vendor categories search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<VendorCategoryResponseDto>>> getAllVendorCategoriesList() {
        log.info("GET /api/vendor-categories/list - Fetching all vendor categories as list");
        List<VendorCategoryResponseDto> vendorCategories = vendorCategoryService.getAllVendorCategoriesList();
        return ResponseBuilder.success(vendorCategories, "Vendor categories list retrieved successfully");
    }

    // ========== Export Endpoint (must be before /{id}) ==========

    @GetMapping("/export")
    @RequireAdmin
    public ResponseEntity<byte[]> exportData() throws java.io.IOException {
        log.info("GET /api/vendor-categories/export - Exporting all vendor categories");
        return bulkUploadHelper.export(vendorCategoryService);
    }

    // ========== CRUD Endpoints ==========

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorCategoryResponseDto>> getVendorCategoryById(@PathVariable Long id) {
        log.info("GET /api/vendor-categories/{} - Fetching vendor category by ID", id);
        VendorCategoryResponseDto vendorCategory = vendorCategoryService.getVendorCategoryById(id);
        return ResponseBuilder.success(vendorCategory, "Vendor category retrieved successfully");
    }

    @PutMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<VendorCategoryResponseDto>> updateVendorCategory(
            @PathVariable Long id,
            @Valid @RequestBody VendorCategoryRequestDto requestDto) {
        log.info("PUT /api/vendor-categories/{} - Updating vendor category", id);
        VendorCategoryResponseDto updatedVendorCategory = vendorCategoryService.updateVendorCategory(id, requestDto);
        return ResponseBuilder.success(updatedVendorCategory, "Vendor category updated successfully");
    }

    @DeleteMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<Void>> deleteVendorCategory(@PathVariable Long id) {
        log.info("DELETE /api/vendor-categories/{} - Deleting vendor category", id);
        vendorCategoryService.deleteVendorCategory(id);
        return ResponseBuilder.success(null, "Vendor category deleted successfully");
    }

    // Bulk Upload Endpoints
    @PostMapping(value = "/bulk/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequireAdmin
    public SseEmitter bulkUploadVendorCategories(
            @RequestParam("file") MultipartFile file) throws java.io.IOException {
        log.info("POST /api/vendor-categories/bulk/upload - Starting bulk upload");
        return bulkUploadHelper.bulkUpload(file, vendorCategoryService);
    }

    @GetMapping("/bulk/export-template")
    @RequireAdmin
    public ResponseEntity<byte[]> downloadBulkUploadTemplate() throws java.io.IOException {
        log.info("GET /api/vendor-categories/bulk/export-template - Downloading bulk upload template");
        return bulkUploadHelper.downloadTemplate(vendorCategoryService);
    }

    @PostMapping("/bulk/export-error-report")
    @RequireAdmin
    public ResponseEntity<byte[]> exportErrorReport(
            @RequestBody BulkUploadProgressDto progressDto) throws java.io.IOException {
        log.info("POST /api/vendor-categories/bulk/export-error-report - Exporting error report");
        return bulkUploadHelper.exportErrors(progressDto, vendorCategoryService);
    }
}
