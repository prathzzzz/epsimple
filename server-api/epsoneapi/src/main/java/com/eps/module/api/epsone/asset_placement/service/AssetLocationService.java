package com.eps.module.api.epsone.asset_placement.service;

import com.eps.module.api.epsone.asset_placement.dto.AssetLocationCheckDto;
import com.eps.module.api.epsone.asset_placement.dto.AssetPlacementBulkUploadDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public interface AssetLocationService {
    AssetLocationCheckDto checkAssetLocation(Long assetId);
    void removeAssetFromCurrentLocation(Long assetId);
    
    // Bulk Upload methods
    SseEmitter bulkUploadPlacements(MultipartFile file) throws IOException;
    ResponseEntity<byte[]> exportPlacementTemplate() throws IOException;
}
