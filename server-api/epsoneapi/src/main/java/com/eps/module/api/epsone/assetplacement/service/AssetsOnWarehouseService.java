package com.eps.module.api.epsone.assetplacement.service;

import com.eps.module.api.epsone.assetplacement.dto.AssetsOnWarehouseRequestDto;
import com.eps.module.api.epsone.assetplacement.dto.AssetsOnWarehouseResponseDto;
import org.springframework.data.domain.Page;

public interface AssetsOnWarehouseService {

    AssetsOnWarehouseResponseDto placeAssetInWarehouse(AssetsOnWarehouseRequestDto requestDto);

    Page<AssetsOnWarehouseResponseDto> getAllAssetsInWarehouse(int page, int size, String sortBy, String sortOrder);

    Page<AssetsOnWarehouseResponseDto> searchAssetsInWarehouse(String searchTerm, int page, int size, String sortBy, String sortOrder);

    Page<AssetsOnWarehouseResponseDto> getAssetsByWarehouseId(Long warehouseId, int page, int size, String sortBy, String sortOrder);

    AssetsOnWarehouseResponseDto getAssetInWarehouseById(Long id);

    AssetsOnWarehouseResponseDto updateAssetInWarehouse(Long id, AssetsOnWarehouseRequestDto requestDto);

    void removeAssetFromWarehouse(Long id);
}
