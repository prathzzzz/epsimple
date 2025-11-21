package com.eps.module.api.epsone.asset_type.controller;

import com.eps.module.api.epsone.asset_type.dto.AssetTypeRequestDto;
import com.eps.module.api.epsone.asset_type.dto.AssetTypeResponseDto;
import com.eps.module.api.epsone.asset_type.service.AssetTypeService;
import com.eps.module.auth.rbac.annotation.RequireAdmin;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/asset-types")
@RequiredArgsConstructor
@RequireAdmin
public class AssetTypeController {

    private final AssetTypeService assetTypeService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    @PostMapping
    public ResponseEntity<ApiResponse<AssetTypeResponseDto>> createAssetType(
            @Valid @RequestBody AssetTypeRequestDto requestDto) {
        AssetTypeResponseDto response = assetTypeService.createAssetType(requestDto);
        return ResponseBuilder.success(response, "Asset type created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssetTypeResponseDto>>> getAllAssetTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<AssetTypeResponseDto> response = assetTypeService.getAllAssetTypes(pageable);
        return ResponseBuilder.success(response, "Asset types retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AssetTypeResponseDto>>> searchAssetTypes(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "typeName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<AssetTypeResponseDto> response = assetTypeService.searchAssetTypes(searchTerm, pageable);
        return ResponseBuilder.success(response, "Asset types search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<AssetTypeResponseDto>>> getAllAssetTypesList() {
        List<AssetTypeResponseDto> response = assetTypeService.getAllAssetTypesList();
        return ResponseBuilder.success(response, "Asset types list retrieved successfully");
    }

    // ========== Export Endpoint (must be before /{id}) ==========

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() throws Exception {
        return bulkUploadControllerHelper.export(assetTypeService);
    }

    // ========== CRUD Endpoints ==========

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetTypeResponseDto>> getAssetTypeById(@PathVariable Long id) {
        AssetTypeResponseDto response = assetTypeService.getAssetTypeById(id);
        return ResponseBuilder.success(response, "Asset type retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetTypeResponseDto>> updateAssetType(
            @PathVariable Long id,
            @Valid @RequestBody AssetTypeRequestDto requestDto) {
        AssetTypeResponseDto response = assetTypeService.updateAssetType(id, requestDto);
        return ResponseBuilder.success(response, "Asset type updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAssetType(@PathVariable Long id) {
        assetTypeService.deleteAssetType(id);
        return ResponseBuilder.success(null, "Asset type deleted successfully");
    }

    // ========== Bulk Upload Endpoints ==========
    
    @PostMapping(value = "/bulk-upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter bulkUploadAssetTypes(@RequestParam("file") MultipartFile file) throws Exception {
        return bulkUploadControllerHelper.bulkUpload(file, assetTypeService);
    }

    @GetMapping("/bulk-upload/template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        return bulkUploadControllerHelper.downloadTemplate(assetTypeService);
    }

    @PostMapping("/bulk-upload/errors")
    public ResponseEntity<byte[]> downloadErrorReport(@RequestBody BulkUploadProgressDto progressData) throws Exception {
        return bulkUploadControllerHelper.exportErrors(progressData, assetTypeService);
    }
}
