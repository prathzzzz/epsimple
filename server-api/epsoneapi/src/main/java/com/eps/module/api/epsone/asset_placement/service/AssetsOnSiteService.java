package com.eps.module.api.epsone.asset_placement.service;

import com.eps.module.api.epsone.asset_placement.dto.AssetsOnSiteRequestDto;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnSiteResponseDto;
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
