package com.eps.module.api.epsone.asset_expenditure_and_activity_work.service;

import com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto.AssetExpenditureAndActivityWorkRequestDto;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto.AssetExpenditureAndActivityWorkResponseDto;
import org.springframework.data.domain.Page;

public interface AssetExpenditureAndActivityWorkService {

    AssetExpenditureAndActivityWorkResponseDto createAssetExpenditureAndActivityWork(AssetExpenditureAndActivityWorkRequestDto requestDto);

    Page<AssetExpenditureAndActivityWorkResponseDto> getAllAssetExpenditureAndActivityWorks(
            int page, int size, String sortBy, String sortOrder);

    Page<AssetExpenditureAndActivityWorkResponseDto> getExpendituresByAssetId(
            Long assetId, int page, int size, String sortBy, String sortOrder);

    Page<AssetExpenditureAndActivityWorkResponseDto> getExpendituresByActivityWorkId(
            Long activityWorkId, int page, int size, String sortBy, String sortOrder);

    Page<AssetExpenditureAndActivityWorkResponseDto> searchExpenditures(
            String searchTerm, int page, int size, String sortBy, String sortOrder);

    AssetExpenditureAndActivityWorkResponseDto getExpenditureById(Long id);

    AssetExpenditureAndActivityWorkResponseDto updateAssetExpenditureAndActivityWork(
            Long id, AssetExpenditureAndActivityWorkRequestDto requestDto);

    void deleteAssetExpenditureAndActivityWork(Long id);
}
