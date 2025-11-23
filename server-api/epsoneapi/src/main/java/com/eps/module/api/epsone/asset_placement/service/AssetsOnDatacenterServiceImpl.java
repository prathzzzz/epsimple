package com.eps.module.api.epsone.asset_placement.service;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_movement.constants.LocationType;
import com.eps.module.api.epsone.asset_movement.service.AssetMovementService;
import com.eps.module.api.epsone.asset_placement.constants.AssetPlacementErrorMessages;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnDatacenterRequestDto;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnDatacenterResponseDto;
import com.eps.module.api.epsone.asset_placement.mapper.AssetsOnDatacenterMapper;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnDatacenterRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnWarehouseRepository;
import com.eps.module.api.epsone.data_center.repository.DatacenterRepository;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.asset.*;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.status.GenericStatusType;
import com.eps.module.warehouse.Datacenter;
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
public class AssetsOnDatacenterServiceImpl implements AssetsOnDatacenterService {

    private final AssetsOnDatacenterRepository assetsOnDatacenterRepository;
    private final AssetsOnSiteRepository assetsOnSiteRepository;
    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;
    private final AssetRepository assetRepository;
    private final DatacenterRepository datacenterRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final AssetsOnDatacenterMapper assetsOnDatacenterMapper;
    private final AssetMovementService assetMovementService;

    @Override
    @Transactional
    public AssetsOnDatacenterResponseDto placeAssetInDatacenter(AssetsOnDatacenterRequestDto requestDto) {
        log.info("Placing asset {} in datacenter {}", requestDto.getAssetId(), requestDto.getDatacenterId());

        // Validate asset exists
        Asset asset = assetRepository.findById(requestDto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.ASSET_NOT_FOUND + requestDto.getAssetId()));

        // Validate datacenter exists
        Datacenter datacenter = datacenterRepository.findById(requestDto.getDatacenterId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.DATACENTER_NOT_FOUND + requestDto.getDatacenterId()));

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
        AssetsOnDatacenter assetsOnDatacenter = assetsOnDatacenterMapper.toEntity(
                requestDto, asset, datacenter, assetStatus, activityWork, null);
        AssetsOnDatacenter saved = assetsOnDatacenterRepository.save(assetsOnDatacenter);

        // Track movement and link to placement
        AssetMovementType movementType = assetMovementService.determineMovementType(fromType, LocationType.DATACENTER_DISPLAY);
        AssetMovementTracker tracker = assetMovementService.trackMovement(
                asset, movementType, fromType.equals(LocationType.NEWLY_PLACED_DISPLAY) ? LocationType.NEWLY_PLACED_DISPLAY : null, fromPlacement, saved);

        // Update placement with tracker
        saved.setAssetMovementTracker(tracker);
        saved = assetsOnDatacenterRepository.save(saved);

        // Fetch with details for response
        AssetsOnDatacenter savedWithDetails = assetsOnDatacenterRepository.findByIdWithDetails(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException(AssetPlacementErrorMessages.ASSET_PLACEMENT_NOT_FOUND_AFTER_SAVE));

        log.info("Asset placed in datacenter successfully with ID: {}", saved.getId());
        return assetsOnDatacenterMapper.toDto(savedWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetsOnDatacenterResponseDto> getAllAssetsInDatacenter(int page, int size, String sortBy, String sortOrder) {
        log.info("Fetching all assets in datacenter: page={}, size={}", page, size);
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetsOnDatacenter> assetsPage = assetsOnDatacenterRepository.findAllWithDetails(pageable);
        return assetsPage.map(assetsOnDatacenterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetsOnDatacenterResponseDto> searchAssetsInDatacenter(String searchTerm, int page, int size, String sortBy, String sortOrder) {
        log.info("Searching assets in datacenter with keyword: {}", searchTerm);
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetsOnDatacenter> assetsPage = assetsOnDatacenterRepository.searchAssetsOnDatacenter(searchTerm, pageable);
        return assetsPage.map(assetsOnDatacenterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetsOnDatacenterResponseDto> getAssetsByDatacenterId(Long datacenterId, int page, int size, String sortBy, String sortOrder) {
        log.info("Fetching assets for datacenter ID: {}", datacenterId);

        // Validate datacenter exists
        if (!datacenterRepository.existsById(datacenterId)) {
            throw new ResourceNotFoundException(AssetPlacementErrorMessages.DATACENTER_NOT_FOUND + datacenterId);
        }

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetsOnDatacenter> assetsPage = assetsOnDatacenterRepository.findByDatacenterIdWithDetails(datacenterId, pageable);
        return assetsPage.map(assetsOnDatacenterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetsOnDatacenterResponseDto getAssetInDatacenterById(Long id) {
        log.info("Fetching asset in datacenter with ID: {}", id);
        AssetsOnDatacenter assetsOnDatacenter = assetsOnDatacenterRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.ASSET_PLACEMENT_NOT_FOUND + id));
        return assetsOnDatacenterMapper.toDto(assetsOnDatacenter);
    }

    @Override
    @Transactional
    public AssetsOnDatacenterResponseDto updateAssetInDatacenter(Long id, AssetsOnDatacenterRequestDto requestDto) {
        log.info("Updating asset in datacenter with ID: {}", id);

        AssetsOnDatacenter assetsOnDatacenter = assetsOnDatacenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.ASSET_PLACEMENT_NOT_FOUND + id));

        // Validate asset exists
        Asset asset = assetRepository.findById(requestDto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.ASSET_NOT_FOUND + requestDto.getAssetId()));

        // Validate datacenter exists
        Datacenter datacenter = datacenterRepository.findById(requestDto.getDatacenterId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        AssetPlacementErrorMessages.DATACENTER_NOT_FOUND + requestDto.getDatacenterId()));

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

        assetsOnDatacenterMapper.updateEntity(assetsOnDatacenter, requestDto, asset, datacenter, assetStatus, activityWork, assetMovementTracker);
        AssetsOnDatacenter updated = assetsOnDatacenterRepository.save(assetsOnDatacenter);

        // Fetch with details for response
        AssetsOnDatacenter updatedWithDetails = assetsOnDatacenterRepository.findByIdWithDetails(updated.getId())
                .orElseThrow(() -> new ResourceNotFoundException(AssetPlacementErrorMessages.ASSET_PLACEMENT_NOT_FOUND_AFTER_UPDATE));

        log.info("Asset in datacenter updated successfully");
        return assetsOnDatacenterMapper.toDto(updatedWithDetails);
    }

    @Override
    @Transactional
    public void removeAssetFromDatacenter(Long id) {
        log.info("Removing asset from datacenter with ID: {}", id);

        if (!assetsOnDatacenterRepository.existsById(id)) {
            throw new ResourceNotFoundException(AssetPlacementErrorMessages.ASSET_PLACEMENT_NOT_FOUND + id);
        }

        assetsOnDatacenterRepository.deleteById(id);
        log.info("Asset removed from datacenter successfully");
    }
}
