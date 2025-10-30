package com.eps.module.api.epsone.asset.service;

import com.eps.module.api.epsone.asset.context.AssetRequestDto;
import com.eps.module.api.epsone.asset.context.AssetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetService {
    AssetResponseDto createAsset(AssetRequestDto requestDto);
    Page<AssetResponseDto> getAllAssets(Pageable pageable);
    Page<AssetResponseDto> searchAssets(String searchTerm, Pageable pageable);
    List<AssetResponseDto> listAssets();
    AssetResponseDto getAssetById(Long id);
    AssetResponseDto updateAsset(Long id, AssetRequestDto requestDto);
    void deleteAsset(Long id);
}
