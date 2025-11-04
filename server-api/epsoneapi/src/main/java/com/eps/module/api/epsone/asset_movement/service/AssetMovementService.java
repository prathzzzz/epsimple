package com.eps.module.api.epsone.asset_movement.service;

import com.eps.module.api.epsone.asset_movement.dto.AssetCurrentLocationDto;
import com.eps.module.api.epsone.asset_movement.dto.AssetMovementHistoryDto;
import com.eps.module.asset.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssetMovementService {

    /**
     * Track a movement from one location to another
     */
    AssetMovementTracker trackMovement(
            Asset asset,
            AssetMovementType movementType,
            String fromFactory,
            Object fromPlacement,  // Can be AssetsOnSite, AssetsOnWarehouse, or AssetsOnDatacenter
            Object toPlacement     // Can be AssetsOnSite, AssetsOnWarehouse, or AssetsOnDatacenter
    );

    /**
     * Get movement history for an asset
     */
    Page<AssetMovementHistoryDto> getAssetMovementHistory(Long assetId, Pageable pageable);

    /**
     * Get current location of an asset
     */
    AssetCurrentLocationDto getCurrentLocation(Long assetId);

    /**
     * Determine movement type based on from and to locations
     */
    AssetMovementType determineMovementType(
            String fromType,
            String toType
    );
}
