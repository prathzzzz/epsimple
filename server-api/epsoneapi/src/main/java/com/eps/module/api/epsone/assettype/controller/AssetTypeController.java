package com.eps.module.api.epsone.assettype.controller;

import com.eps.module.api.epsone.assettype.dto.AssetTypeRequestDto;
import com.eps.module.api.epsone.assettype.dto.AssetTypeResponseDto;
import com.eps.module.api.epsone.assettype.service.AssetTypeService;
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
@RequestMapping("/api/asset-types")
@RequiredArgsConstructor
public class AssetTypeController {

    private final AssetTypeService assetTypeService;

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
}
