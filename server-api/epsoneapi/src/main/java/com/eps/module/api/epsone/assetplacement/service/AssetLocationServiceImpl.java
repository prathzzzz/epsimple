package com.eps.module.api.epsone.assetplacement.service;

import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.assetplacement.dto.AssetLocationCheckDto;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnDatacenterRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnWarehouseRepository;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetsOnDatacenter;
import com.eps.module.asset.AssetsOnSite;
import com.eps.module.asset.AssetsOnWarehouse;
import com.eps.module.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetLocationServiceImpl implements AssetLocationService {

    private static final String ASSET_NOT_FOUND_MSG = "Asset not found with id: ";

    private final AssetsOnSiteRepository assetsOnSiteRepository;
    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;
    private final AssetsOnDatacenterRepository assetsOnDatacenterRepository;
    private final AssetRepository assetRepository;

    @Override
    @Transactional(readOnly = true)
    public AssetLocationCheckDto checkAssetLocation(Long assetId) {
        log.info("Checking location for asset ID: {}", assetId);

        // Validate asset exists
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ASSET_NOT_FOUND_MSG + assetId));

        // Check if asset is on a site
        Optional<AssetsOnSite> siteLocation = assetsOnSiteRepository.findByAssetId(assetId);
        if (siteLocation.isPresent()) {
            AssetsOnSite placement = siteLocation.get();
            return AssetLocationCheckDto.builder()
                    .isPlaced(true)
                    .locationType("site")
                    .locationId(placement.getSite().getId())
                    .locationName(placement.getSite().getSiteCode())
                    .locationCode(placement.getSite().getSiteCode())
                    .assetTagId(asset.getAssetTagId())
                    .build();
        }

        // Check if asset is in a warehouse
        Optional<AssetsOnWarehouse> warehouseLocation = assetsOnWarehouseRepository.findByAssetId(assetId);
        if (warehouseLocation.isPresent()) {
            AssetsOnWarehouse placement = warehouseLocation.get();
            return AssetLocationCheckDto.builder()
                    .isPlaced(true)
                    .locationType("warehouse")
                    .locationId(placement.getWarehouse().getId())
                    .locationName(placement.getWarehouse().getWarehouseName())
                    .locationCode(placement.getWarehouse().getWarehouseCode())
                    .assetTagId(asset.getAssetTagId())
                    .build();
        }

        // Check if asset is in a datacenter
        Optional<AssetsOnDatacenter> datacenterLocation = assetsOnDatacenterRepository.findByAssetId(assetId);
        if (datacenterLocation.isPresent()) {
            AssetsOnDatacenter placement = datacenterLocation.get();
            return AssetLocationCheckDto.builder()
                    .isPlaced(true)
                    .locationType("datacenter")
                    .locationId(placement.getDatacenter().getId())
                    .locationName(placement.getDatacenter().getDatacenterName())
                    .locationCode(placement.getDatacenter().getDatacenterCode())
                    .assetTagId(asset.getAssetTagId())
                    .build();
        }

        // Asset is not placed anywhere
        return AssetLocationCheckDto.builder()
                .isPlaced(false)
                .assetTagId(asset.getAssetTagId())
                .build();
    }

    @Override
    @Transactional
    public void removeAssetFromCurrentLocation(Long assetId) {
        log.info("Removing asset {} from current location", assetId);

        // Validate asset exists
        assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ASSET_NOT_FOUND_MSG + assetId));

        // Check and remove from site
        assetsOnSiteRepository.findByAssetId(assetId).ifPresent(placement -> {
            log.info("Removing asset {} from site {}", assetId, placement.getSite().getSiteCode());
            assetsOnSiteRepository.delete(placement);
        });

        // Check and remove from warehouse
        assetsOnWarehouseRepository.findByAssetId(assetId).ifPresent(placement -> {
            log.info("Removing asset {} from warehouse {}", assetId, placement.getWarehouse().getWarehouseCode());
            assetsOnWarehouseRepository.delete(placement);
        });

        // Check and remove from datacenter
        assetsOnDatacenterRepository.findByAssetId(assetId).ifPresent(placement -> {
            log.info("Removing asset {} from datacenter {}", assetId, placement.getDatacenter().getDatacenterCode());
            assetsOnDatacenterRepository.delete(placement);
        });

        log.info("Asset {} removed from current location successfully", assetId);
    }
}
