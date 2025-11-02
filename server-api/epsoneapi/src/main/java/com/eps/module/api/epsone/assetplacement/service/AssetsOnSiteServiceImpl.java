package com.eps.module.api.epsone.assetplacement.service;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activitywork.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.assetplacement.dto.AssetsOnSiteRequestDto;
import com.eps.module.api.epsone.assetplacement.dto.AssetsOnSiteResponseDto;
import com.eps.module.api.epsone.assetplacement.mapper.AssetsOnSiteMapper;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnWarehouseRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnDatacenterRepository;
import com.eps.module.api.epsone.genericstatustype.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetMovementTracker;
import com.eps.module.asset.AssetsOnSite;
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

        // Check if asset is already placed anywhere (site, warehouse, or datacenter)
        if (assetsOnSiteRepository.existsByAssetId(requestDto.getAssetId())) {
            throw new IllegalArgumentException(
                    "Asset '" + asset.getAssetTagId() + "' is already placed on a site. Remove it from the current location first.");
        }
        if (assetsOnWarehouseRepository.existsByAssetId(requestDto.getAssetId())) {
            throw new IllegalArgumentException(
                    "Asset '" + asset.getAssetTagId() + "' is already placed in a warehouse. Remove it from the current location first.");
        }
        if (assetsOnDatacenterRepository.existsByAssetId(requestDto.getAssetId())) {
            throw new IllegalArgumentException(
                    "Asset '" + asset.getAssetTagId() + "' is already placed in a datacenter. Remove it from the current location first.");
        }

        // Validate optional activity work if provided
        ActivityWork activityWork = null;
        if (requestDto.getActivityWorkId() != null) {
            activityWork = activityWorkRepository.findById(requestDto.getActivityWorkId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Activity work not found with id: " + requestDto.getActivityWorkId()));
        }

        // For now, assetMovementTracker is null (movement tracking is separate)
        AssetMovementTracker assetMovementTracker = null;

        AssetsOnSite assetsOnSite = assetsOnSiteMapper.toEntity(
                requestDto, asset, site, assetStatus, activityWork, assetMovementTracker);
        AssetsOnSite saved = assetsOnSiteRepository.save(assetsOnSite);

        // Fetch with details for response
        AssetsOnSite savedWithDetails = assetsOnSiteRepository.findByIdWithDetails(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset placement not found after save"));

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
            throw new ResourceNotFoundException("Site not found with id: " + siteId);
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
                        "Asset placement not found with id: " + id));
        return assetsOnSiteMapper.toDto(assetsOnSite);
    }

    @Override
    @Transactional
    public AssetsOnSiteResponseDto updateAssetOnSite(Long id, AssetsOnSiteRequestDto requestDto) {
        log.info("Updating asset on site with ID: {}", id);

        AssetsOnSite assetsOnSite = assetsOnSiteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asset placement not found with id: " + id));

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

        // Validate optional activity work if provided
        ActivityWork activityWork = null;
        if (requestDto.getActivityWorkId() != null) {
            activityWork = activityWorkRepository.findById(requestDto.getActivityWorkId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Activity work not found with id: " + requestDto.getActivityWorkId()));
        }

        AssetMovementTracker assetMovementTracker = null;

        assetsOnSiteMapper.updateEntity(assetsOnSite, requestDto, asset, site, assetStatus, activityWork, assetMovementTracker);
        AssetsOnSite updated = assetsOnSiteRepository.save(assetsOnSite);

        // Fetch with details for response
        AssetsOnSite updatedWithDetails = assetsOnSiteRepository.findByIdWithDetails(updated.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset placement not found after update"));

        log.info("Asset on site updated successfully");
        return assetsOnSiteMapper.toDto(updatedWithDetails);
    }

    @Override
    @Transactional
    public void removeAssetFromSite(Long id) {
        log.info("Removing asset from site with ID: {}", id);

        if (!assetsOnSiteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Asset placement not found with id: " + id);
        }

        assetsOnSiteRepository.deleteById(id);
        log.info("Asset removed from site successfully");
    }
}
