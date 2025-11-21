package com.eps.module.api.epsone.asset_placement.controller;

import com.eps.module.api.epsone.asset_placement.dto.AssetsOnSiteRequestDto;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnSiteResponseDto;
import com.eps.module.api.epsone.asset_placement.service.AssetsOnSiteService;
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
@RequestMapping("/api/assets-on-site")
@RequiredArgsConstructor
public class AssetsOnSiteController {

    private final AssetsOnSiteService assetsOnSiteService;

    @PostMapping
    @RequirePermission("ASSETS_ON_SITE:CREATE")
    public ResponseEntity<ApiResponse<AssetsOnSiteResponseDto>> placeAssetOnSite(
            @Valid @RequestBody AssetsOnSiteRequestDto requestDto) {
        log.info("POST /api/assets-on-site - Placing asset on site");
        AssetsOnSiteResponseDto response = assetsOnSiteService.placeAssetOnSite(requestDto);
        return ResponseBuilder.success(response, "Asset placed on site successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssetsOnSiteResponseDto>>> getAllAssetsOnSite(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/assets-on-site - Fetching all assets on site: page={}, size={}", page, size);
        Page<AssetsOnSiteResponseDto> response = assetsOnSiteService.getAllAssetsOnSite(page, size, sortBy, sortOrder);
        return ResponseBuilder.success(response, "Assets on site retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AssetsOnSiteResponseDto>>> searchAssetsOnSite(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/assets-on-site/search - Searching assets on site with term: {}", searchTerm);
        Page<AssetsOnSiteResponseDto> response = assetsOnSiteService.searchAssetsOnSite(searchTerm, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(response, "Assets on site search completed", HttpStatus.OK);
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<ApiResponse<Page<AssetsOnSiteResponseDto>>> getAssetsBySiteId(
            @PathVariable Long siteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/assets-on-site/site/{} - Fetching assets for site", siteId);
        Page<AssetsOnSiteResponseDto> response = assetsOnSiteService.getAssetsBySiteId(siteId, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(response, "Assets for site retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetsOnSiteResponseDto>> getAssetOnSiteById(@PathVariable Long id) {
        log.info("GET /api/assets-on-site/{} - Fetching asset on site by ID", id);
        AssetsOnSiteResponseDto response = assetsOnSiteService.getAssetOnSiteById(id);
        return ResponseBuilder.success(response, "Asset on site retrieved successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @RequirePermission("ASSETS_ON_SITE:UPDATE")
    public ResponseEntity<ApiResponse<AssetsOnSiteResponseDto>> updateAssetOnSite(
            @PathVariable Long id,
            @Valid @RequestBody AssetsOnSiteRequestDto requestDto) {
        log.info("PUT /api/assets-on-site/{} - Updating asset on site", id);
        AssetsOnSiteResponseDto response = assetsOnSiteService.updateAssetOnSite(id, requestDto);
        return ResponseBuilder.success(response, "Asset on site updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @RequirePermission("ASSETS_ON_SITE:DELETE")
    public ResponseEntity<ApiResponse<Void>> removeAssetFromSite(@PathVariable Long id) {
        log.info("DELETE /api/assets-on-site/{} - Removing asset from site", id);
        assetsOnSiteService.removeAssetFromSite(id);
        return ResponseBuilder.success(null, "Asset removed from site successfully", HttpStatus.OK);
    }
}
