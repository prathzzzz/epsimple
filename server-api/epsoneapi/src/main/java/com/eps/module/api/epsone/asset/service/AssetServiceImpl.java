package com.eps.module.api.epsone.asset.service;

import com.eps.module.api.epsone.asset.context.AssetRequestDto;
import com.eps.module.api.epsone.asset.context.AssetResponseDto;
import com.eps.module.api.epsone.asset.mapper.AssetMapper;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
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

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;

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
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        return assetMapper.toDto(asset);
    }

    @Override
    @Transactional
    public AssetResponseDto updateAsset(Long id, AssetRequestDto requestDto) {
        Asset existingAsset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

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
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        
        // TODO: Add dependency protection when placement repositories are implemented
        // Check AssetsOnSite, AssetsOnWarehouse, AssetsOnDatacenter
        // Example error message:
        // "Cannot delete asset 'ATMHDbob00001' because it is placed on 2 site(s): Site-001, Site-002. 
        //  Please remove the asset from these locations first."
        
        // TODO: Check AssetMovementTracker for movement history
        // TODO: Check AssetExpenditureAndActivityWork for expenditure records
        
        assetRepository.deleteById(id);
    }
}
