package com.eps.module.api.epsone.cost_type.controller;

import com.eps.module.api.epsone.cost_type.dto.CostTypeRequestDto;
import com.eps.module.api.epsone.cost_type.dto.CostTypeResponseDto;
import com.eps.module.api.epsone.cost_type.service.CostTypeService;
import com.eps.module.auth.rbac.annotation.RequireAdmin;
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
@RequestMapping("/api/cost-types")
@RequiredArgsConstructor
@RequireAdmin
public class CostTypeController {

    private final CostTypeService costTypeService;
    private final BulkUploadControllerHelper bulkUploadHelper;

    @PostMapping
    public ResponseEntity<ApiResponse<CostTypeResponseDto>> createCostType(
            @Valid @RequestBody CostTypeRequestDto requestDto) {
        CostTypeResponseDto response = costTypeService.createCostType(requestDto);
        return ResponseBuilder.success(response, "Cost type created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CostTypeResponseDto>>> getAllCostTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<CostTypeResponseDto> response = costTypeService.getAllCostTypes(pageable);
        return ResponseBuilder.success(response, "Cost types retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CostTypeResponseDto>>> searchCostTypes(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "typeName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<CostTypeResponseDto> response = costTypeService.searchCostTypes(searchTerm, pageable);
        return ResponseBuilder.success(response, "Cost types search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<CostTypeResponseDto>>> getAllCostTypesList() {
        List<CostTypeResponseDto> response = costTypeService.getAllCostTypesList();
        return ResponseBuilder.success(response, "Cost types list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CostTypeResponseDto>> getCostTypeById(@PathVariable Long id) {
        CostTypeResponseDto response = costTypeService.getCostTypeById(id);
        return ResponseBuilder.success(response, "Cost type retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CostTypeResponseDto>> updateCostType(
            @PathVariable Long id,
            @Valid @RequestBody CostTypeRequestDto requestDto) {
        CostTypeResponseDto response = costTypeService.updateCostType(id, requestDto);
        return ResponseBuilder.success(response, "Cost type updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCostType(@PathVariable Long id) {
        costTypeService.deleteCostType(id);
        return ResponseBuilder.success(null, "Cost type deleted successfully");
    }

    // Bulk endpoints

    @PostMapping(value = "/bulk-upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public org.springframework.web.servlet.mvc.method.annotation.SseEmitter bulkUploadCostTypes(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        return bulkUploadHelper.bulkUpload(file, costTypeService);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCostTypes() throws java.io.IOException {
        return bulkUploadHelper.export(costTypeService);
    }

    @GetMapping("/download-template")
    public ResponseEntity<byte[]> downloadTemplate() throws java.io.IOException {
        return bulkUploadHelper.downloadTemplate(costTypeService);
    }

    @PostMapping("/export-errors")
    public ResponseEntity<byte[]> exportBulkUploadErrors(
            @RequestBody com.eps.module.common.bulk.dto.BulkUploadProgressDto progressData) throws java.io.IOException {
        return bulkUploadHelper.exportErrors(progressData, costTypeService);
    }
}
