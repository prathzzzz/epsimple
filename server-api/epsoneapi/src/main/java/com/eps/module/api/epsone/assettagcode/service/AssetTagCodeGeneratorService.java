package com.eps.module.api.epsone.assettagcode.service;

import com.eps.module.api.epsone.assettagcode.dto.AssetTagCodeGeneratorRequestDto;
import com.eps.module.api.epsone.assettagcode.dto.AssetTagCodeGeneratorResponseDto;
import com.eps.module.api.epsone.assettagcode.dto.GeneratedAssetTagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetTagCodeGeneratorService {

    AssetTagCodeGeneratorResponseDto createGenerator(AssetTagCodeGeneratorRequestDto dto);

    Page<AssetTagCodeGeneratorResponseDto> getAllGenerators(Pageable pageable);

    Page<AssetTagCodeGeneratorResponseDto> searchGenerators(String searchTerm, Pageable pageable);

    List<AssetTagCodeGeneratorResponseDto> getAllGeneratorsAsList();

    AssetTagCodeGeneratorResponseDto getGeneratorById(Long id);

    AssetTagCodeGeneratorResponseDto updateGenerator(Long id, AssetTagCodeGeneratorRequestDto dto);

    void deleteGenerator(Long id);

    GeneratedAssetTagDto generateAssetTag(Long assetCategoryId, Long vendorId, Long bankId);

    GeneratedAssetTagDto previewAssetTag(Long assetCategoryId, Long vendorId, Long bankId);
}
