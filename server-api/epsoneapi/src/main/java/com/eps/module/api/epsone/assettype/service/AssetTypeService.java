package com.eps.module.api.epsone.assettype.service;

import com.eps.module.api.epsone.assettype.dto.AssetTypeRequestDto;
import com.eps.module.api.epsone.assettype.dto.AssetTypeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetTypeService {
    AssetTypeResponseDto createAssetType(AssetTypeRequestDto requestDto);
    AssetTypeResponseDto updateAssetType(Long id, AssetTypeRequestDto requestDto);
    void deleteAssetType(Long id);
    AssetTypeResponseDto getAssetTypeById(Long id);
    Page<AssetTypeResponseDto> getAllAssetTypes(Pageable pageable);
    Page<AssetTypeResponseDto> searchAssetTypes(String search, Pageable pageable);
    List<AssetTypeResponseDto> getAllAssetTypesList();
}
