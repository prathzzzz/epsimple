package com.eps.module.api.epsone.asset.service;

import com.eps.module.api.epsone.asset.context.AssetRequestDto;
import com.eps.module.api.epsone.asset.context.AssetResponseDto;
import com.eps.module.api.epsone.asset.mapper.AssetMapper;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnWarehouseRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnDatacenterRepository;
import com.eps.module.asset.Asset;
import com.eps.module.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private static final String ASSET_NOT_FOUND_WITH_ID_MSG = "Asset not found with id: ";

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final AssetsOnSiteRepository assetsOnSiteRepository;
    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;
    private final AssetsOnDatacenterRepository assetsOnDatacenterRepository;

    @Override
    @Transactional
    public AssetResponseDto createAsset(AssetRequestDto requestDto) {
        // Validate asset tag uniqueness
        if (requestDto.getAssetTagId() != null && 
            assetRepository.findByAssetTagId(requestDto.getAssetTagId()).isPresent()) {
            throw new IllegalArgumentException("Asset tag already exists: " + requestDto.getAssetTagId());
        }

        // Validate serial number uniqueness if provided
        if (requestDto.getSerialNumber() != null && 
            assetRepository.findBySerialNumber(requestDto.getSerialNumber()).isPresent()) {
            throw new IllegalArgumentException("Serial number already exists: " + requestDto.getSerialNumber());
        }

        Asset asset = assetMapper.toEntity(requestDto);
        Asset savedAsset = assetRepository.save(asset);
        
        // Reload with all relationships
        return assetMapper.toDto(
            assetRepository.findById(savedAsset.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found after save"))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetResponseDto> getAllAssets(Pageable pageable) {
        return assetRepository.findAll(pageable)
                .map(assetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetResponseDto> searchAssets(String searchTerm, Pageable pageable) {
        return assetRepository.search(searchTerm, pageable)
                .map(assetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetResponseDto> listAssets() {
        return assetRepository.findAllWithDetails().stream()
                .map(assetMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AssetResponseDto getAssetById(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ASSET_NOT_FOUND_WITH_ID_MSG + id));
        return assetMapper.toDto(asset);
    }

    @Override
    @Transactional
    public AssetResponseDto updateAsset(Long id, AssetRequestDto requestDto) {
        Asset existingAsset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ASSET_NOT_FOUND_WITH_ID_MSG + id));

        // Validate asset tag uniqueness (if changed)
        if (requestDto.getAssetTagId() != null && 
            !requestDto.getAssetTagId().equals(existingAsset.getAssetTagId())) {
            assetRepository.findByAssetTagId(requestDto.getAssetTagId()).ifPresent(asset -> {
                throw new IllegalArgumentException("Asset tag already exists: " + requestDto.getAssetTagId());
            });
        }

        // Validate serial number uniqueness (if changed)
        if (requestDto.getSerialNumber() != null && 
            !requestDto.getSerialNumber().equals(existingAsset.getSerialNumber())) {
            assetRepository.findBySerialNumber(requestDto.getSerialNumber()).ifPresent(asset -> {
                throw new IllegalArgumentException("Serial number already exists: " + requestDto.getSerialNumber());
            });
        }

        assetMapper.updateEntityFromDto(requestDto, existingAsset);
        Asset updatedAsset = assetRepository.save(existingAsset);
        
        // Reload with all relationships
        return assetMapper.toDto(
            assetRepository.findById(updatedAsset.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found after update"))
        );
    }

    @Override
    @Transactional
    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ASSET_NOT_FOUND_WITH_ID_MSG + id));
        
        // Check for placements on sites
        long siteCount = assetsOnSiteRepository.countByAssetId(id);
        if (siteCount > 0) {
            throw new IllegalStateException(String.format(
                "Cannot delete asset '%s' because it is placed on %d site%s. Please remove the asset from these sites first.",
                asset.getAssetTagId(), siteCount, siteCount > 1 ? "s" : ""
            ));
        }
        
        // Check for placements in warehouses
        long warehouseCount = assetsOnWarehouseRepository.countByAssetId(id);
        if (warehouseCount > 0) {
            throw new IllegalStateException(String.format(
                "Cannot delete asset '%s' because it is placed in %d warehouse%s. Please remove the asset from these warehouses first.",
                asset.getAssetTagId(), warehouseCount, warehouseCount > 1 ? "s" : ""
            ));
        }
        
        // Check for placements in datacenters
        long datacenterCount = assetsOnDatacenterRepository.countByAssetId(id);
        if (datacenterCount > 0) {
            throw new IllegalStateException(String.format(
                "Cannot delete asset '%s' because it is placed in %d datacenter%s. Please remove the asset from these datacenters first.",
                asset.getAssetTagId(), datacenterCount, datacenterCount > 1 ? "s" : ""
            ));
        }
        
        assetRepository.deleteById(id);
    }
}
