package com.eps.module.api.epsone.assetplacement.service;

import com.eps.module.api.epsone.assetplacement.dto.AssetLocationCheckDto;

public interface AssetLocationService {
    AssetLocationCheckDto checkAssetLocation(Long assetId);
    void removeAssetFromCurrentLocation(Long assetId);
}
