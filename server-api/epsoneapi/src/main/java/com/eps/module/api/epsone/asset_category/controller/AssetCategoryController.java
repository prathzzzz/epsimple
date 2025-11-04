package com.eps.module.api.epsone.asset_category.controller;

import com.eps.module.api.epsone.asset_category.dto.AssetCategoryRequestDto;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryResponseDto;
import com.eps.module.api.epsone.asset_category.service.AssetCategoryService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/asset-categories")
@RequiredArgsConstructor
public class AssetCategoryController {

    private final AssetCategoryService assetCategoryService;

    @PostMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetCategoryResponseDto>> getAssetCategoryById(@PathVariable Long id) {
        log.info("GET /api/asset-categories/{} - Fetching asset category by ID", id);
        AssetCategoryResponseDto assetCategory = assetCategoryService.getAssetCategoryById(id);
        return ResponseBuilder.success(assetCategory, "Asset category retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetCategoryResponseDto>> updateAssetCategory(
            @PathVariable Long id,
            @Valid @RequestBody AssetCategoryRequestDto requestDto) {
        log.info("PUT /api/asset-categories/{} - Updating asset category", id);
        AssetCategoryResponseDto updatedAssetCategory = assetCategoryService.updateAssetCategory(id, requestDto);
        return ResponseBuilder.success(updatedAssetCategory, "Asset category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAssetCategory(@PathVariable Long id) {
        log.info("DELETE /api/asset-categories/{} - Deleting asset category", id);
        assetCategoryService.deleteAssetCategory(id);
        return ResponseBuilder.success(null, "Asset category deleted successfully");
    }
}
