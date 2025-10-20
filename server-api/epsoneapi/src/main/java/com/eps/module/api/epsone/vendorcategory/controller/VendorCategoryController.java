package com.eps.module.api.epsone.vendorcategory.controller;

import com.eps.module.api.epsone.vendorcategory.dto.VendorCategoryRequestDto;
import com.eps.module.api.epsone.vendorcategory.dto.VendorCategoryResponseDto;
import com.eps.module.api.epsone.vendorcategory.service.VendorCategoryService;
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
@RequestMapping("/api/vendor-categories")
@RequiredArgsConstructor
public class VendorCategoryController {

    private final VendorCategoryService vendorCategoryService;

    @PostMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorCategoryResponseDto>> getVendorCategoryById(@PathVariable Long id) {
        log.info("GET /api/vendor-categories/{} - Fetching vendor category by ID", id);
        VendorCategoryResponseDto vendorCategory = vendorCategoryService.getVendorCategoryById(id);
        return ResponseBuilder.success(vendorCategory, "Vendor category retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorCategoryResponseDto>> updateVendorCategory(
            @PathVariable Long id,
            @Valid @RequestBody VendorCategoryRequestDto requestDto) {
        log.info("PUT /api/vendor-categories/{} - Updating vendor category", id);
        VendorCategoryResponseDto updatedVendorCategory = vendorCategoryService.updateVendorCategory(id, requestDto);
        return ResponseBuilder.success(updatedVendorCategory, "Vendor category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVendorCategory(@PathVariable Long id) {
        log.info("DELETE /api/vendor-categories/{} - Deleting vendor category", id);
        vendorCategoryService.deleteVendorCategory(id);
        return ResponseBuilder.success(null, "Vendor category deleted successfully");
    }
}
