package com.eps.module.api.epsone.asset_placement.service;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_movement.constants.LocationType;
import com.eps.module.api.epsone.asset_movement.service.AssetMovementService;
import com.eps.module.api.epsone.asset_placement.constants.AssetPlacementErrorMessages;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnWarehouseRequestDto;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnWarehouseResponseDto;
import com.eps.module.api.epsone.asset_placement.mapper.AssetsOnWarehouseMapper;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnWarehouseRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnDatacenterRepository;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.warehouse.repository.WarehouseRepository;
import com.eps.module.asset.*;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.status.GenericStatusType;
import com.eps.module.warehouse.Warehouse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetsOnWarehouseServiceImpl implements AssetsOnWarehouseService {

    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;
    private final AssetsOnSiteRepository assetsOnSiteRepository;
    private final AssetsOnDatacenterRepository assetsOnDatacenterRepository;
    private final AssetRepository assetRepository;
    private final WarehouseRepository warehouseRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final AssetsOnWarehouseMapper assetsOnWarehouseMapper;
    private final AssetMovementService assetMovementService;

    @Override
    @Transactional
    public AssetsOnWarehouseResponseDto placeAssetInWarehouse(AssetsOnWarehouseRequestDto requestDto) {
        log.info("Placing asset {} in warehouse {}", requestDto.getAssetId(), requestDto.getWarehouseId());

        // Validate asset exists
        Asset asset = assetRepository.findById(requestDto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.ASSET_NOT_FOUND + requestDto.getAssetId()));

        // Validate warehouse exists
        Warehouse warehouse = warehouseRepository.findById(requestDto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.WAREHOUSE_NOT_FOUND + requestDto.getWarehouseId()));

        // Validate asset status exists
        GenericStatusType assetStatus = genericStatusTypeRepository.findById(requestDto.getAssetStatusId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.ASSET_STATUS_NOT_FOUND + requestDto.getAssetStatusId()));

        // Check for active placements and handle movement tracking
        Object fromPlacement = null;
        String fromType = LocationType.NEWLY_PLACED_DISPLAY;
        
        // Check if asset has active placement on site
        Optional<AssetsOnSite> activeSite = assetsOnSiteRepository.findActiveByAssetId(requestDto.getAssetId());
        if (activeSite.isPresent()) {
            fromPlacement = activeSite.get();
            fromType = LocationType.SITE_DISPLAY;
            // Mark old placement as vacated
            activeSite.get().setVacatedOn(LocalDate.now());
            assetsOnSiteRepository.save(activeSite.get());
        }
        
        // Check if asset has active placement in warehouse
        Optional<AssetsOnWarehouse> activeWarehouse = assetsOnWarehouseRepository.findActiveByAssetId(requestDto.getAssetId());
        if (activeWarehouse.isPresent()) {
            fromPlacement = activeWarehouse.get();
            fromType = LocationType.WAREHOUSE_DISPLAY;
            // Mark old placement as vacated
            activeWarehouse.get().setVacatedOn(LocalDate.now());
            assetsOnWarehouseRepository.save(activeWarehouse.get());
        }
        
        // Check if asset has active placement in datacenter
        Optional<AssetsOnDatacenter> activeDatacenter = assetsOnDatacenterRepository.findActiveByAssetId(requestDto.getAssetId());
        if (activeDatacenter.isPresent()) {
            fromPlacement = activeDatacenter.get();
            fromType = LocationType.DATACENTER_DISPLAY;
            // Mark old placement as vacated
            activeDatacenter.get().setVacatedOn(LocalDate.now());
            assetsOnDatacenterRepository.save(activeDatacenter.get());
        }

        // Validate optional activity work if provided
        ActivityWork activityWork = null;
        if (requestDto.getActivityWorkId() != null) {
            activityWork = activityWorkRepository.findById(requestDto.getActivityWorkId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            AssetPlacementErrorMessages.ACTIVITY_WORK_NOT_FOUND + requestDto.getActivityWorkId()));
        }

        // Create new placement
        AssetsOnWarehouse assetsOnWarehouse = assetsOnWarehouseMapper.toEntity(
                requestDto, asset, warehouse, assetStatus, activityWork, null);
        AssetsOnWarehouse saved = assetsOnWarehouseRepository.save(assetsOnWarehouse);

        // Track movement and link to placement
        AssetMovementType movementType = assetMovementService.determineMovementType(fromType, LocationType.WAREHOUSE_DISPLAY);
        AssetMovementTracker tracker = assetMovementService.trackMovement(
                asset, movementType, fromType.equals(LocationType.NEWLY_PLACED_DISPLAY) ? LocationType.NEWLY_PLACED_DISPLAY : null, fromPlacement, saved);
        
        // Update placement with tracker
        saved.setAssetMovementTracker(tracker);
        saved = assetsOnWarehouseRepository.save(saved);

        // Fetch with details for response
        AssetsOnWarehouse savedWithDetails = assetsOnWarehouseRepository.findByIdWithDetails(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException(AssetPlacementErrorMessages.ASSET_PLACEMENT_NOT_FOUND_AFTER_SAVE));

        log.info("Asset placed in warehouse successfully with ID: {}", saved.getId());
        return assetsOnWarehouseMapper.toDto(savedWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetsOnWarehouseResponseDto> getAllAssetsInWarehouse(int page, int size, String sortBy, String sortOrder) {
        log.info("Fetching all assets in warehouse: page={}, size={}", page, size);
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetsOnWarehouse> assetsPage = assetsOnWarehouseRepository.findAllWithDetails(pageable);
        return assetsPage.map(assetsOnWarehouseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetsOnWarehouseResponseDto> searchAssetsInWarehouse(String searchTerm, int page, int size, String sortBy, String sortOrder) {
        log.info("Searching assets in warehouse with keyword: {}", searchTerm);
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetsOnWarehouse> assetsPage = assetsOnWarehouseRepository.searchAssetsOnWarehouse(searchTerm, pageable);
        return assetsPage.map(assetsOnWarehouseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetsOnWarehouseResponseDto> getAssetsByWarehouseId(Long warehouseId, int page, int size, String sortBy, String sortOrder) {
        log.info("Fetching assets for warehouse ID: {}", warehouseId);
        
        // Validate warehouse exists
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException(AssetPlacementErrorMessages.WAREHOUSE_NOT_FOUND + warehouseId);
        }

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetsOnWarehouse> assetsPage = assetsOnWarehouseRepository.findByWarehouseIdWithDetails(warehouseId, pageable);
        return assetsPage.map(assetsOnWarehouseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetsOnWarehouseResponseDto getAssetInWarehouseById(Long id) {
        log.info("Fetching asset in warehouse with ID: {}", id);
        AssetsOnWarehouse assetsOnWarehouse = assetsOnWarehouseRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.ASSET_PLACEMENT_NOT_FOUND + id));
        return assetsOnWarehouseMapper.toDto(assetsOnWarehouse);
    }

    @Override
    @Transactional
    public AssetsOnWarehouseResponseDto updateAssetInWarehouse(Long id, AssetsOnWarehouseRequestDto requestDto) {
        log.info("Updating asset in warehouse with ID: {}", id);

        AssetsOnWarehouse assetsOnWarehouse = assetsOnWarehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.ASSET_PLACEMENT_NOT_FOUND + id));

        // Validate asset exists
        Asset asset = assetRepository.findById(requestDto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.ASSET_NOT_FOUND + requestDto.getAssetId()));

        // Validate warehouse exists
        Warehouse warehouse = warehouseRepository.findById(requestDto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.WAREHOUSE_NOT_FOUND + requestDto.getWarehouseId()));

        // Validate asset status exists
        GenericStatusType assetStatus = genericStatusTypeRepository.findById(requestDto.getAssetStatusId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.ASSET_STATUS_NOT_FOUND + requestDto.getAssetStatusId()));

        // Validate optional activity work if provided
        ActivityWork activityWork = null;
        if (requestDto.getActivityWorkId() != null) {
            activityWork = activityWorkRepository.findById(requestDto.getActivityWorkId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            AssetPlacementErrorMessages.ACTIVITY_WORK_NOT_FOUND + requestDto.getActivityWorkId()));
        }

        AssetMovementTracker assetMovementTracker = null;

        assetsOnWarehouseMapper.updateEntity(assetsOnWarehouse, requestDto, asset, warehouse, assetStatus, activityWork, assetMovementTracker);
        AssetsOnWarehouse updated = assetsOnWarehouseRepository.save(assetsOnWarehouse);

        // Fetch with details for response
        AssetsOnWarehouse updatedWithDetails = assetsOnWarehouseRepository.findByIdWithDetails(updated.getId())
                .orElseThrow(() -> new ResourceNotFoundException(AssetPlacementErrorMessages.ASSET_PLACEMENT_NOT_FOUND_AFTER_UPDATE));

        log.info("Asset in warehouse updated successfully");
        return assetsOnWarehouseMapper.toDto(updatedWithDetails);
    }

    @Override
    @Transactional
    public void removeAssetFromWarehouse(Long id) {
        log.info("Removing asset from warehouse with ID: {}", id);

        if (!assetsOnWarehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException(AssetPlacementErrorMessages.ASSET_PLACEMENT_NOT_FOUND + id);
        }

        assetsOnWarehouseRepository.deleteById(id);
        log.info("Asset removed from warehouse successfully");
    }
}
