package com.eps.module.api.epsone.asset_placement.service;

import com.eps.module.api.epsone.asset_placement.dto.AssetLocationCheckDto;

public interface AssetLocationService {
    AssetLocationCheckDto checkAssetLocation(Long assetId);
    void removeAssetFromCurrentLocation(Long assetId);
}
