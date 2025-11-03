package com.eps.module.api.epsone.assetmovement.service;

import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.assetmovement.dto.AssetCurrentLocationDto;
import com.eps.module.api.epsone.assetmovement.dto.AssetMovementHistoryDto;
import com.eps.module.api.epsone.assetmovement.mapper.AssetMovementMapper;
import com.eps.module.api.epsone.assetmovement.repository.AssetMovementTrackerRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnDatacenterRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnWarehouseRepository;
import com.eps.module.api.epsone.movementtype.repository.MovementTypeRepository;
import com.eps.module.asset.*;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.site.Site;
import com.eps.module.warehouse.Datacenter;
import com.eps.module.warehouse.Warehouse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetMovementServiceImpl implements AssetMovementService {

    private final AssetMovementTrackerRepository movementTrackerRepository;
    private final AssetsOnSiteRepository assetsOnSiteRepository;
    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;
    private final AssetsOnDatacenterRepository assetsOnDatacenterRepository;
    private final AssetRepository assetRepository;
    private final MovementTypeRepository movementTypeRepository;
    private final AssetMovementMapper movementMapper;

    @Override
    @Transactional
    public AssetMovementTracker trackMovement(
            Asset asset,
            AssetMovementType movementType,
            String fromFactory,
            Object fromPlacement,
            Object toPlacement) {

        log.info("Tracking movement for asset {} with type {}", asset.getAssetTagId(), movementType.getMovementType());

        AssetMovementTracker.AssetMovementTrackerBuilder builder = AssetMovementTracker.builder()
                .asset(asset)
                .assetMovementType(movementType);

        // Set from location
        if (fromFactory != null && !fromFactory.isEmpty()) {
            builder.fromFactory(fromFactory);
        } else if (fromPlacement != null) {
            setFromLocation(builder, fromPlacement);
        }

        // Set to location
        if (toPlacement != null) {
            setToLocation(builder, toPlacement);
        }

        AssetMovementTracker tracker = builder.build();
        AssetMovementTracker saved = movementTrackerRepository.save(tracker);

        log.info("Movement tracked successfully with ID: {}", saved.getId());
        return saved;
    }

    private void setFromLocation(AssetMovementTracker.AssetMovementTrackerBuilder builder, Object fromPlacement) {
        if (fromPlacement instanceof AssetsOnSite) {
            Site site = ((AssetsOnSite) fromPlacement).getSite();
            builder.fromSite(site);
        } else if (fromPlacement instanceof AssetsOnWarehouse) {
            Warehouse warehouse = ((AssetsOnWarehouse) fromPlacement).getWarehouse();
            builder.fromWarehouse(warehouse);
        } else if (fromPlacement instanceof AssetsOnDatacenter) {
            Datacenter datacenter = ((AssetsOnDatacenter) fromPlacement).getDatacenter();
            builder.fromDatacenter(datacenter);
        }
    }

    private void setToLocation(AssetMovementTracker.AssetMovementTrackerBuilder builder, Object toPlacement) {
        if (toPlacement instanceof AssetsOnSite) {
            Site site = ((AssetsOnSite) toPlacement).getSite();
            builder.toSite(site);
        } else if (toPlacement instanceof AssetsOnWarehouse) {
            Warehouse warehouse = ((AssetsOnWarehouse) toPlacement).getWarehouse();
            builder.toWarehouse(warehouse);
        } else if (toPlacement instanceof AssetsOnDatacenter) {
            Datacenter datacenter = ((AssetsOnDatacenter) toPlacement).getDatacenter();
            builder.toDatacenter(datacenter);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetMovementHistoryDto> getAssetMovementHistory(Long assetId, Pageable pageable) {
        log.info("Fetching movement history for asset ID: {}", assetId);

        // Verify asset exists
        assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + assetId));

        // Get page of movements (IDs only)
        Page<AssetMovementTracker> movementsPage = movementTrackerRepository.findByAssetIdWithDetails(assetId, pageable);
        
        // If no movements, return empty page
        if (movementsPage.isEmpty()) {
            return movementsPage.map(movementMapper::toHistoryDto);
        }
        
        // Fetch full details for movements on this page
        List<Long> ids = movementsPage.getContent().stream()
                .map(AssetMovementTracker::getId)
                .collect(java.util.stream.Collectors.toList());
        
        List<AssetMovementTracker> movementsWithDetails = movementTrackerRepository.findByIdsWithDetails(ids);
        
        // Map to DTOs maintaining the original order
        java.util.Map<Long, AssetMovementTracker> detailsMap = movementsWithDetails.stream()
                .collect(java.util.stream.Collectors.toMap(AssetMovementTracker::getId, m -> m));
        
        List<AssetMovementHistoryDto> dtos = movementsPage.getContent().stream()
                .map(m -> movementMapper.toHistoryDto(detailsMap.get(m.getId())))
                .collect(java.util.stream.Collectors.toList());
        
        return new org.springframework.data.domain.PageImpl<>(
                dtos, 
                pageable, 
                movementsPage.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AssetCurrentLocationDto getCurrentLocation(Long assetId) {
        log.info("Fetching current location for asset ID: {}", assetId);

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + assetId));

        // Check site
        Optional<AssetsOnSite> siteOpt = assetsOnSiteRepository.findActiveByAssetId(assetId);
        if (siteOpt.isPresent()) {
            return movementMapper.toCurrentLocationDto(asset, siteOpt.get(), "site");
        }

        // Check warehouse
        Optional<AssetsOnWarehouse> warehouseOpt = assetsOnWarehouseRepository.findActiveByAssetId(assetId);
        if (warehouseOpt.isPresent()) {
            return movementMapper.toCurrentLocationDto(asset, warehouseOpt.get(), "warehouse");
        }

        // Check datacenter
        Optional<AssetsOnDatacenter> datacenterOpt = assetsOnDatacenterRepository.findActiveByAssetId(assetId);
        if (datacenterOpt.isPresent()) {
            return movementMapper.toCurrentLocationDto(asset, datacenterOpt.get(), "datacenter");
        }

        // Not placed anywhere
        return AssetCurrentLocationDto.builder()
                .assetId(asset.getId())
                .assetTagId(asset.getAssetTagId())
                .assetName(asset.getAssetName())
                .isPlaced(false)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AssetMovementType determineMovementType(String fromType, String toType) {
        String movementTypeName = buildMovementTypeName(fromType, toType);
        
        return movementTypeRepository.findByMovementType(movementTypeName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Movement type not found: " + movementTypeName));
    }

    private String buildMovementTypeName(String fromType, String toType) {
        String from = capitalizeFirstLetter(fromType);
        String to = capitalizeFirstLetter(toType);
        return from + " to " + to;
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
