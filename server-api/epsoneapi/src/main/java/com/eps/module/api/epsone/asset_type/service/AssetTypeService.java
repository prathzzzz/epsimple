package com.eps.module.api.epsone.asset_type.service;

import com.eps.module.api.epsone.asset_type.dto.AssetTypeBulkUploadDto;
import com.eps.module.api.epsone.asset_type.dto.AssetTypeRequestDto;
import com.eps.module.api.epsone.asset_type.dto.AssetTypeResponseDto;
import com.eps.module.asset.AssetType;
import com.eps.module.common.bulk.service.BulkUploadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetTypeService extends BulkUploadService<AssetTypeBulkUploadDto, AssetType> {
    AssetTypeResponseDto createAssetType(AssetTypeRequestDto requestDto);
    AssetTypeResponseDto updateAssetType(Long id, AssetTypeRequestDto requestDto);
    void deleteAssetType(Long id);
    AssetTypeResponseDto getAssetTypeById(Long id);
    Page<AssetTypeResponseDto> getAllAssetTypes(Pageable pageable);
    Page<AssetTypeResponseDto> searchAssetTypes(String search, Pageable pageable);
    List<AssetTypeResponseDto> getAllAssetTypesList();
}
