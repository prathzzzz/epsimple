package com.eps.module.api.epsone.assetplacement.service;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activitywork.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.assetmovement.constants.LocationType;
import com.eps.module.api.epsone.assetmovement.service.AssetMovementService;
import com.eps.module.api.epsone.assetplacement.constants.ErrorMessages;
import com.eps.module.api.epsone.assetplacement.dto.AssetsOnSiteRequestDto;
import com.eps.module.api.epsone.assetplacement.dto.AssetsOnSiteResponseDto;
import com.eps.module.api.epsone.assetplacement.mapper.AssetsOnSiteMapper;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnWarehouseRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnDatacenterRepository;
import com.eps.module.api.epsone.genericstatustype.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.asset.*;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.site.Site;
import com.eps.module.status.GenericStatusType;
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
public class AssetsOnSiteServiceImpl implements AssetsOnSiteService {

    private final AssetsOnSiteRepository assetsOnSiteRepository;
    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;
    private final AssetsOnDatacenterRepository assetsOnDatacenterRepository;
    private final AssetRepository assetRepository;
    private final SiteRepository siteRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final AssetsOnSiteMapper assetsOnSiteMapper;
    private final AssetMovementService assetMovementService;

    @Override
    @Transactional
    public AssetsOnSiteResponseDto placeAssetOnSite(AssetsOnSiteRequestDto requestDto) {
        log.info("Placing asset {} on site {}", requestDto.getAssetId(), requestDto.getSiteId());

        // Validate asset exists
        Asset asset = assetRepository.findById(requestDto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asset not found with id: " + requestDto.getAssetId()));

        // Validate site exists
        Site site = siteRepository.findById(requestDto.getSiteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Site not found with id: " + requestDto.getSiteId()));

        // Validate asset status exists
        GenericStatusType assetStatus = genericStatusTypeRepository.findById(requestDto.getAssetStatusId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asset status not found with id: " + requestDto.getAssetStatusId()));

        // Check for active placements and handle movement tracking
        Object fromPlacement = null;
        String fromType = LocationType.FACTORY_DISPLAY;
        
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
                            ErrorMessages.ACTIVITY_WORK_NOT_FOUND + requestDto.getActivityWorkId()));
        }

        // Create new placement
        AssetsOnSite assetsOnSite = assetsOnSiteMapper.toEntity(
                requestDto, asset, site, assetStatus, activityWork, null);
        AssetsOnSite saved = assetsOnSiteRepository.save(assetsOnSite);

        // Track movement and link to placement
        AssetMovementType movementType = assetMovementService.determineMovementType(fromType, LocationType.SITE_DISPLAY);
        AssetMovementTracker tracker = assetMovementService.trackMovement(
                asset, movementType, fromType.equals(LocationType.FACTORY_DISPLAY) ? LocationType.FACTORY_DISPLAY : null, fromPlacement, saved);
        
        // Update placement with tracker
        saved.setAssetMovementTracker(tracker);
        saved = assetsOnSiteRepository.save(saved);

        // Fetch with details for response
        AssetsOnSite savedWithDetails = assetsOnSiteRepository.findByIdWithDetails(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ASSET_PLACEMENT_NOT_FOUND_AFTER_SAVE));

        log.info("Asset placed on site successfully with ID: {}", saved.getId());
        return assetsOnSiteMapper.toDto(savedWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetsOnSiteResponseDto> getAllAssetsOnSite(int page, int size, String sortBy, String sortOrder) {
        log.info("Fetching all assets on site: page={}, size={}", page, size);
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetsOnSite> assetsPage = assetsOnSiteRepository.findAllWithDetails(pageable);
        return assetsPage.map(assetsOnSiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetsOnSiteResponseDto> searchAssetsOnSite(String searchTerm, int page, int size, String sortBy, String sortOrder) {
        log.info("Searching assets on site with keyword: {}", searchTerm);
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetsOnSite> assetsPage = assetsOnSiteRepository.searchAssetsOnSite(searchTerm, pageable);
        return assetsPage.map(assetsOnSiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetsOnSiteResponseDto> getAssetsBySiteId(Long siteId, int page, int size, String sortBy, String sortOrder) {
        log.info("Fetching assets for site ID: {}", siteId);
        
        // Validate site exists
        if (!siteRepository.existsById(siteId)) {
            throw new ResourceNotFoundException(ErrorMessages.SITE_NOT_FOUND + siteId);
        }

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetsOnSite> assetsPage = assetsOnSiteRepository.findBySiteIdWithDetails(siteId, pageable);
        return assetsPage.map(assetsOnSiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetsOnSiteResponseDto getAssetOnSiteById(Long id) {
        log.info("Fetching asset on site with ID: {}", id);
        AssetsOnSite assetsOnSite = assetsOnSiteRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.ASSET_PLACEMENT_NOT_FOUND + id));
        return assetsOnSiteMapper.toDto(assetsOnSite);
    }

    @Override
    @Transactional
    public AssetsOnSiteResponseDto updateAssetOnSite(Long id, AssetsOnSiteRequestDto requestDto) {
        log.info("Updating asset on site with ID: {}", id);

        AssetsOnSite assetsOnSite = assetsOnSiteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.ASSET_PLACEMENT_NOT_FOUND + id));

        // Validate asset exists
        Asset asset = assetRepository.findById(requestDto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.ASSET_NOT_FOUND + requestDto.getAssetId()));

        // Validate site exists
        Site site = siteRepository.findById(requestDto.getSiteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.SITE_NOT_FOUND + requestDto.getSiteId()));

        // Validate asset status exists
        GenericStatusType assetStatus = genericStatusTypeRepository.findById(requestDto.getAssetStatusId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.ASSET_STATUS_NOT_FOUND + requestDto.getAssetStatusId()));

        // Validate optional activity work if provided
        ActivityWork activityWork = null;
        if (requestDto.getActivityWorkId() != null) {
            activityWork = activityWorkRepository.findById(requestDto.getActivityWorkId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            ErrorMessages.ACTIVITY_WORK_NOT_FOUND + requestDto.getActivityWorkId()));
        }

        AssetMovementTracker assetMovementTracker = null;

        assetsOnSiteMapper.updateEntity(assetsOnSite, requestDto, asset, site, assetStatus, activityWork, assetMovementTracker);
        AssetsOnSite updated = assetsOnSiteRepository.save(assetsOnSite);

        // Fetch with details for response
        AssetsOnSite updatedWithDetails = assetsOnSiteRepository.findByIdWithDetails(updated.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ASSET_PLACEMENT_NOT_FOUND_AFTER_UPDATE));

        log.info("Asset on site updated successfully");
        return assetsOnSiteMapper.toDto(updatedWithDetails);
    }

    @Override
    @Transactional
    public void removeAssetFromSite(Long id) {
        log.info("Removing asset from site with ID: {}", id);

        if (!assetsOnSiteRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorMessages.ASSET_PLACEMENT_NOT_FOUND + id);
        }

        assetsOnSiteRepository.deleteById(id);
        log.info("Asset removed from site successfully");
    }
}
