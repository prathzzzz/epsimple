package com.eps.module.api.epsone.assetplacement.service;

import com.eps.module.api.epsone.assetplacement.dto.AssetsOnSiteRequestDto;
import com.eps.module.api.epsone.assetplacement.dto.AssetsOnSiteResponseDto;
import org.springframework.data.domain.Page;

public interface AssetsOnSiteService {

    AssetsOnSiteResponseDto placeAssetOnSite(AssetsOnSiteRequestDto requestDto);

    Page<AssetsOnSiteResponseDto> getAllAssetsOnSite(int page, int size, String sortBy, String sortOrder);

    Page<AssetsOnSiteResponseDto> searchAssetsOnSite(String searchTerm, int page, int size, String sortBy, String sortOrder);

    Page<AssetsOnSiteResponseDto> getAssetsBySiteId(Long siteId, int page, int size, String sortBy, String sortOrder);

    AssetsOnSiteResponseDto getAssetOnSiteById(Long id);

    AssetsOnSiteResponseDto updateAssetOnSite(Long id, AssetsOnSiteRequestDto requestDto);

    void removeAssetFromSite(Long id);
}
