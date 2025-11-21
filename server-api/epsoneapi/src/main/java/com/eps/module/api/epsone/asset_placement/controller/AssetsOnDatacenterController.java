package com.eps.module.api.epsone.asset_placement.controller;

import com.eps.module.api.epsone.asset_placement.dto.AssetsOnDatacenterRequestDto;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnDatacenterResponseDto;
import com.eps.module.api.epsone.asset_placement.service.AssetsOnDatacenterService;
import com.eps.module.auth.rbac.annotation.RequirePermission;
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
@RequestMapping("/api/assets-on-datacenter")
@RequiredArgsConstructor
public class AssetsOnDatacenterController {

    private final AssetsOnDatacenterService assetsOnDatacenterService;

    @PostMapping
    @RequirePermission("ASSETS_ON_DATACENTER:CREATE")
    public ResponseEntity<ApiResponse<AssetsOnDatacenterResponseDto>> placeAssetInDatacenter(
            @Valid @RequestBody AssetsOnDatacenterRequestDto requestDto) {
        log.info("POST /api/assets-on-datacenter - Placing asset in datacenter");
        AssetsOnDatacenterResponseDto response = assetsOnDatacenterService.placeAssetInDatacenter(requestDto);
        return ResponseBuilder.success(response, "Asset placed in datacenter successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssetsOnDatacenterResponseDto>>> getAllAssetsInDatacenter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/assets-on-datacenter - Fetching all assets in datacenter: page={}, size={}", page, size);
        Page<AssetsOnDatacenterResponseDto> response = assetsOnDatacenterService.getAllAssetsInDatacenter(page, size, sortBy, sortOrder);
        return ResponseBuilder.success(response, "Assets in datacenter retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AssetsOnDatacenterResponseDto>>> searchAssetsInDatacenter(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/assets-on-datacenter/search - Searching assets in datacenter with term: {}", searchTerm);
        Page<AssetsOnDatacenterResponseDto> response = assetsOnDatacenterService.searchAssetsInDatacenter(searchTerm, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(response, "Assets in datacenter search completed", HttpStatus.OK);
    }

    @GetMapping("/datacenter/{datacenterId}")
    public ResponseEntity<ApiResponse<Page<AssetsOnDatacenterResponseDto>>> getAssetsByDatacenterId(
            @PathVariable Long datacenterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/assets-on-datacenter/datacenter/{} - Fetching assets for datacenter", datacenterId);
        Page<AssetsOnDatacenterResponseDto> response = assetsOnDatacenterService.getAssetsByDatacenterId(datacenterId, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(response, "Assets for datacenter retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetsOnDatacenterResponseDto>> getAssetInDatacenterById(@PathVariable Long id) {
        log.info("GET /api/assets-on-datacenter/{} - Fetching asset in datacenter by ID", id);
        AssetsOnDatacenterResponseDto response = assetsOnDatacenterService.getAssetInDatacenterById(id);
        return ResponseBuilder.success(response, "Asset in datacenter retrieved successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @RequirePermission("ASSETS_ON_DATACENTER:UPDATE")
    public ResponseEntity<ApiResponse<AssetsOnDatacenterResponseDto>> updateAssetInDatacenter(
            @PathVariable Long id,
            @Valid @RequestBody AssetsOnDatacenterRequestDto requestDto) {
        log.info("PUT /api/assets-on-datacenter/{} - Updating asset in datacenter", id);
        AssetsOnDatacenterResponseDto response = assetsOnDatacenterService.updateAssetInDatacenter(id, requestDto);
        return ResponseBuilder.success(response, "Asset in datacenter updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @RequirePermission("ASSETS_ON_DATACENTER:DELETE")
    public ResponseEntity<ApiResponse<Void>> removeAssetFromDatacenter(@PathVariable Long id) {
        log.info("DELETE /api/assets-on-datacenter/{} - Removing asset from datacenter", id);
        assetsOnDatacenterService.removeAssetFromDatacenter(id);
        return ResponseBuilder.success(null, "Asset removed from datacenter successfully", HttpStatus.OK);
    }
}
