package com.eps.module.api.epsone.asset_category.controller;

import com.eps.module.api.epsone.asset_category.dto.AssetCategoryRequestDto;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryResponseDto;
import com.eps.module.api.epsone.asset_category.service.AssetCategoryService;
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
@RequestMapping("/api/asset-categories")
@RequiredArgsConstructor
public class AssetCategoryController {

    private final AssetCategoryService assetCategoryService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    @PostMapping
    @RequireAdmin
    public ResponseEntity<ApiResponse<AssetCategoryResponseDto>> createAssetCategory(
            @Valid @RequestBody AssetCategoryRequestDto requestDto) {
        log.info("POST /api/asset-categories - Creating new asset category");
        AssetCategoryResponseDto createdAssetCategory = assetCategoryService.createAssetCategory(requestDto);
        return ResponseBuilder.success(createdAssetCategory, "Asset category created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssetCategoryResponseDto>>> getAllAssetCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        log.info("GET /api/asset-categories - Fetching all asset categories with pagination");

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<AssetCategoryResponseDto> assetCategories = assetCategoryService.getAllAssetCategories(pageable);

        return ResponseBuilder.success(assetCategories, "Asset categories retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AssetCategoryResponseDto>>> searchAssetCategories(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categoryName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        log.info("GET /api/asset-categories/search - Searching asset categories with term: {}", searchTerm);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<AssetCategoryResponseDto> assetCategories = assetCategoryService.searchAssetCategories(searchTerm, pageable);

        return ResponseBuilder.success(assetCategories, "Asset categories search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<AssetCategoryResponseDto>>> getAllAssetCategoriesList() {
        log.info("GET /api/asset-categories/list - Fetching all asset categories as list");
        List<AssetCategoryResponseDto> assetCategories = assetCategoryService.getAllAssetCategoriesList();
        return ResponseBuilder.success(assetCategories, "Asset categories list retrieved successfully");
    }

    // ========== Export Endpoint (must be before /{id}) ==========

    @GetMapping("/export")
    @RequireAdmin
    public ResponseEntity<byte[]> exportData() throws Exception {
        log.info("GET /api/asset-categories/export - Exporting all asset categories");
        return bulkUploadControllerHelper.export(assetCategoryService);
    }

    // ========== CRUD Endpoints ==========

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetCategoryResponseDto>> getAssetCategoryById(@PathVariable Long id) {
        log.info("GET /api/asset-categories/{} - Fetching asset category by ID", id);
        AssetCategoryResponseDto assetCategory = assetCategoryService.getAssetCategoryById(id);
        return ResponseBuilder.success(assetCategory, "Asset category retrieved successfully");
    }

    @PutMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<AssetCategoryResponseDto>> updateAssetCategory(
            @PathVariable Long id,
            @Valid @RequestBody AssetCategoryRequestDto requestDto) {
        log.info("PUT /api/asset-categories/{} - Updating asset category", id);
        AssetCategoryResponseDto updatedAssetCategory = assetCategoryService.updateAssetCategory(id, requestDto);
        return ResponseBuilder.success(updatedAssetCategory, "Asset category updated successfully");
    }

    @DeleteMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<Void>> deleteAssetCategory(@PathVariable Long id) {
        log.info("DELETE /api/asset-categories/{} - Deleting asset category", id);
        assetCategoryService.deleteAssetCategory(id);
        return ResponseBuilder.success(null, "Asset category deleted successfully");
    }

    // ========== Bulk Upload Endpoints ==========

    @PostMapping(value = "/bulk-upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @RequireAdmin
    public SseEmitter bulkUploadAssetCategories(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("POST /api/asset-categories/bulk-upload - Starting bulk upload");
        return bulkUploadControllerHelper.bulkUpload(file, assetCategoryService);
    }

    @GetMapping("/bulk-upload/template")
    @RequireAdmin
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        log.info("GET /api/asset-categories/bulk-upload/template - Downloading template");
        return bulkUploadControllerHelper.downloadTemplate(assetCategoryService);
    }

    @PostMapping("/bulk-upload/errors")
    @RequireAdmin
    public ResponseEntity<byte[]> downloadErrorReport(@RequestBody BulkUploadProgressDto progressData) throws Exception {
        log.info("POST /api/asset-categories/bulk-upload/errors - Downloading error report");
        return bulkUploadControllerHelper.exportErrors(progressData, assetCategoryService);
    }
}
