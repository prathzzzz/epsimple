package com.eps.module.api.epsone.asset_placement.controller;

import com.eps.module.api.epsone.asset_placement.dto.AssetLocationCheckDto;
import com.eps.module.api.epsone.asset_placement.service.AssetLocationService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/asset-location")
@RequiredArgsConstructor
public class AssetLocationController {

    private final AssetLocationService assetLocationService;

    @GetMapping("/check/{assetId}")
    public ResponseEntity<ApiResponse<AssetLocationCheckDto>> checkAssetLocation(@PathVariable Long assetId) {
        log.info("GET /api/asset-location/check/{} - Checking asset location", assetId);
        AssetLocationCheckDto response = assetLocationService.checkAssetLocation(assetId);
        return ResponseBuilder.success(response, "Asset location check completed", HttpStatus.OK);
    }

    @DeleteMapping("/remove/{assetId}")
    public ResponseEntity<ApiResponse<Void>> removeAssetFromCurrentLocation(@PathVariable Long assetId) {
        log.info("DELETE /api/asset-location/remove/{} - Removing asset from current location", assetId);
        assetLocationService.removeAssetFromCurrentLocation(assetId);
        return ResponseBuilder.success(null, "Asset removed from current location successfully", HttpStatus.OK);
    }
}
