package com.eps.module.api.epsone.assetplacement.controller;

import com.eps.module.api.epsone.assetplacement.dto.AssetsOnWarehouseRequestDto;
import com.eps.module.api.epsone.assetplacement.dto.AssetsOnWarehouseResponseDto;
import com.eps.module.api.epsone.assetplacement.service.AssetsOnWarehouseService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/assets-on-warehouse")
@RequiredArgsConstructor
public class AssetsOnWarehouseController {

    private final AssetsOnWarehouseService assetsOnWarehouseService;

    @PostMapping
    public ResponseEntity<ApiResponse<AssetsOnWarehouseResponseDto>> placeAssetInWarehouse(
            @Valid @RequestBody AssetsOnWarehouseRequestDto requestDto) {
        log.info("POST /api/assets-on-warehouse - Placing asset in warehouse");
        AssetsOnWarehouseResponseDto response = assetsOnWarehouseService.placeAssetInWarehouse(requestDto);
        return ResponseBuilder.success(response, "Asset placed in warehouse successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssetsOnWarehouseResponseDto>>> getAllAssetsInWarehouse(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/assets-on-warehouse - Fetching all assets in warehouse: page={}, size={}", page, size);
        Page<AssetsOnWarehouseResponseDto> response = assetsOnWarehouseService.getAllAssetsInWarehouse(page, size, sortBy, sortOrder);
        return ResponseBuilder.success(response, "Assets in warehouse retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AssetsOnWarehouseResponseDto>>> searchAssetsInWarehouse(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/assets-on-warehouse/search - Searching assets in warehouse with term: {}", searchTerm);
        Page<AssetsOnWarehouseResponseDto> response = assetsOnWarehouseService.searchAssetsInWarehouse(searchTerm, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(response, "Assets in warehouse search completed", HttpStatus.OK);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<ApiResponse<Page<AssetsOnWarehouseResponseDto>>> getAssetsByWarehouseId(
            @PathVariable Long warehouseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/assets-on-warehouse/warehouse/{} - Fetching assets for warehouse", warehouseId);
        Page<AssetsOnWarehouseResponseDto> response = assetsOnWarehouseService.getAssetsByWarehouseId(warehouseId, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(response, "Assets for warehouse retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetsOnWarehouseResponseDto>> getAssetInWarehouseById(@PathVariable Long id) {
        log.info("GET /api/assets-on-warehouse/{} - Fetching asset in warehouse by ID", id);
        AssetsOnWarehouseResponseDto response = assetsOnWarehouseService.getAssetInWarehouseById(id);
        return ResponseBuilder.success(response, "Asset in warehouse retrieved successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetsOnWarehouseResponseDto>> updateAssetInWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody AssetsOnWarehouseRequestDto requestDto) {
        log.info("PUT /api/assets-on-warehouse/{} - Updating asset in warehouse", id);
        AssetsOnWarehouseResponseDto response = assetsOnWarehouseService.updateAssetInWarehouse(id, requestDto);
        return ResponseBuilder.success(response, "Asset in warehouse updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> removeAssetFromWarehouse(@PathVariable Long id) {
        log.info("DELETE /api/assets-on-warehouse/{} - Removing asset from warehouse", id);
        assetsOnWarehouseService.removeAssetFromWarehouse(id);
        return ResponseBuilder.success(null, "Asset removed from warehouse successfully", HttpStatus.OK);
    }
}
