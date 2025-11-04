package com.eps.module.api.epsone.asset_placement.service;

import com.eps.module.api.epsone.asset_placement.dto.AssetsOnDatacenterRequestDto;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnDatacenterResponseDto;
import org.springframework.data.domain.Page;

public interface AssetsOnDatacenterService {

    AssetsOnDatacenterResponseDto placeAssetInDatacenter(AssetsOnDatacenterRequestDto requestDto);

    Page<AssetsOnDatacenterResponseDto> getAllAssetsInDatacenter(int page, int size, String sortBy, String sortOrder);

    Page<AssetsOnDatacenterResponseDto> searchAssetsInDatacenter(String searchTerm, int page, int size, String sortBy, String sortOrder);

    Page<AssetsOnDatacenterResponseDto> getAssetsByDatacenterId(Long datacenterId, int page, int size, String sortBy, String sortOrder);

    AssetsOnDatacenterResponseDto getAssetInDatacenterById(Long id);

    AssetsOnDatacenterResponseDto updateAssetInDatacenter(Long id, AssetsOnDatacenterRequestDto requestDto);

    void removeAssetFromDatacenter(Long id);
}
