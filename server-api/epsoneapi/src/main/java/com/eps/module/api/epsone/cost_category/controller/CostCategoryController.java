package com.eps.module.api.epsone.cost_category.controller;

import com.eps.module.api.epsone.cost_category.dto.CostCategoryRequestDto;
import com.eps.module.api.epsone.cost_category.dto.CostCategoryResponseDto;
import com.eps.module.api.epsone.cost_category.service.CostCategoryService;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cost-categories")
@RequiredArgsConstructor
public class CostCategoryController {

    private final CostCategoryService costCategoryService;
    private final BulkUploadControllerHelper bulkUploadHelper;

    @PostMapping
    public ResponseEntity<ApiResponse<CostCategoryResponseDto>> createCostCategory(
            @Valid @RequestBody CostCategoryRequestDto requestDto) {
        CostCategoryResponseDto response = costCategoryService.createCostCategory(requestDto);
        return ResponseBuilder.success(response, "Cost category created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CostCategoryResponseDto>>> getAllCostCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<CostCategoryResponseDto> response = costCategoryService.getAllCostCategories(pageable);
        return ResponseBuilder.success(response, "Cost categories retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CostCategoryResponseDto>>> searchCostCategories(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categoryName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<CostCategoryResponseDto> response = costCategoryService.searchCostCategories(searchTerm, pageable);
        return ResponseBuilder.success(response, "Cost categories search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<CostCategoryResponseDto>>> getAllCostCategoriesList() {
        List<CostCategoryResponseDto> response = costCategoryService.getAllCostCategoriesList();
        return ResponseBuilder.success(response, "Cost categories list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CostCategoryResponseDto>> getCostCategoryById(@PathVariable Long id) {
        CostCategoryResponseDto response = costCategoryService.getCostCategoryById(id);
        return ResponseBuilder.success(response, "Cost category retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CostCategoryResponseDto>> updateCostCategory(
            @PathVariable Long id,
            @Valid @RequestBody CostCategoryRequestDto requestDto) {
        CostCategoryResponseDto response = costCategoryService.updateCostCategory(id, requestDto);
        return ResponseBuilder.success(response, "Cost category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCostCategory(@PathVariable Long id) {
        costCategoryService.deleteCostCategory(id);
        return ResponseBuilder.success(null, "Cost category deleted successfully");
    }

    // Bulk endpoints

    @PostMapping(value = "/bulk-upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public org.springframework.web.servlet.mvc.method.annotation.SseEmitter bulkUploadCostCategories(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        return bulkUploadHelper.bulkUpload(file, costCategoryService);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCostCategories() throws java.io.IOException {
        return bulkUploadHelper.export(costCategoryService);
    }

    @GetMapping("/download-template")
    public ResponseEntity<byte[]> downloadTemplate() throws java.io.IOException {
        return bulkUploadHelper.downloadTemplate(costCategoryService);
    }

    @PostMapping("/export-errors")
    public ResponseEntity<byte[]> exportBulkUploadErrors(
            @RequestBody com.eps.module.common.bulk.dto.BulkUploadProgressDto progressData) throws java.io.IOException {
        return bulkUploadHelper.exportErrors(progressData, costCategoryService);
    }
}
