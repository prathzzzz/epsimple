package com.eps.module.api.epsone.asset.service;

import com.eps.module.api.epsone.asset.dto.AssetFinancialDetailsDto;
import com.eps.module.api.epsone.asset.dto.AssetFinancialExportRequestDto;
import com.eps.module.api.epsone.asset.dto.AssetFinancialExportRowDto;
import com.eps.module.api.epsone.asset.dto.AssetRequestDto;
import com.eps.module.api.epsone.asset.dto.AssetResponseDto;
import com.eps.module.api.epsone.asset.dto.AssetBulkUploadDto;
import com.eps.module.asset.Asset;
import com.eps.module.common.bulk.service.BulkUploadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetService extends BulkUploadService<AssetBulkUploadDto, Asset> {
    AssetResponseDto createAsset(AssetRequestDto requestDto);

    Page<AssetResponseDto> getAllAssets(Pageable pageable);

    Page<AssetResponseDto> searchAssets(String searchTerm, Pageable pageable);

    List<AssetResponseDto> listAssets();

    AssetResponseDto getAssetById(Long id);

    AssetResponseDto updateAsset(Long id, AssetRequestDto requestDto);

    void deleteAsset(Long id);

    AssetFinancialDetailsDto getAssetFinancialDetails(Long id);

    List<AssetFinancialExportRowDto> getFinancialExportData(AssetFinancialExportRequestDto requestDto);

    void scrapAsset(Long assetId);

    void unscrapAsset(Long assetId);
}
