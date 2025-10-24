package com.eps.module.api.epsone.assetcategory.service;

import com.eps.module.api.epsone.assetcategory.dto.AssetCategoryRequestDto;
import com.eps.module.api.epsone.assetcategory.dto.AssetCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetCategoryService {

    AssetCategoryResponseDto createAssetCategory(AssetCategoryRequestDto requestDto);

    Page<AssetCategoryResponseDto> getAllAssetCategories(Pageable pageable);

    Page<AssetCategoryResponseDto> searchAssetCategories(String searchTerm, Pageable pageable);

    List<AssetCategoryResponseDto> getAllAssetCategoriesList();

    AssetCategoryResponseDto getAssetCategoryById(Long id);

    AssetCategoryResponseDto updateAssetCategory(Long id, AssetCategoryRequestDto requestDto);

    void deleteAssetCategory(Long id);
}
