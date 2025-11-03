package com.eps.module.api.epsone.assetmovement.controller;

import com.eps.module.api.epsone.assetmovement.dto.AssetCurrentLocationDto;
import com.eps.module.api.epsone.assetmovement.dto.AssetMovementHistoryDto;
import com.eps.module.api.epsone.assetmovement.service.AssetMovementService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetMovementController {

    private final AssetMovementService assetMovementService;

    @GetMapping("/{assetId}/movement-history")
    public ResponseEntity<ApiResponse<Page<AssetMovementHistoryDto>>> getAssetMovementHistory(
            @PathVariable Long assetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        
        log.info("GET /api/assets/{}/movement-history - Fetching movement history", assetId);
        
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<AssetMovementHistoryDto> history = assetMovementService.getAssetMovementHistory(assetId, pageable);
        
        return ResponseBuilder.success(history, "Asset movement history retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/{assetId}/current-location")
    public ResponseEntity<ApiResponse<AssetCurrentLocationDto>> getCurrentLocation(@PathVariable Long assetId) {
        log.info("GET /api/assets/{}/current-location - Fetching current location", assetId);
        
        AssetCurrentLocationDto location = assetMovementService.getCurrentLocation(assetId);
        
        return ResponseBuilder.success(location, "Asset current location retrieved successfully", HttpStatus.OK);
    }
}
