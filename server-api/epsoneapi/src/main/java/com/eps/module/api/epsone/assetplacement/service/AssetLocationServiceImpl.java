package com.eps.module.api.epsone.assetplacement.service;

import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.assetplacement.constants.ErrorMessages;
import com.eps.module.api.epsone.assetplacement.dto.AssetLocationCheckDto;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnDatacenterRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnWarehouseRepository;
import com.eps.module.api.epsone.assetmovement.constants.LocationType;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetsOnDatacenter;
import com.eps.module.asset.AssetsOnSite;
import com.eps.module.asset.AssetsOnWarehouse;
import com.eps.module.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetLocationServiceImpl implements AssetLocationService {

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
                        ErrorMessages.ASSET_NOT_FOUND + assetId));

        // Check if asset is on a site (active placement only)
        Optional<AssetsOnSite> siteLocation = assetsOnSiteRepository.findActiveByAssetId(assetId);
        if (siteLocation.isPresent()) {
            AssetsOnSite placement = siteLocation.get();
            return AssetLocationCheckDto.builder()
                    .isPlaced(true)
                    .locationType(LocationType.SITE)
                    .locationId(placement.getSite().getId())
                    .locationName(placement.getSite().getSiteCode())
                    .locationCode(placement.getSite().getSiteCode())
                    .assetTagId(asset.getAssetTagId())
                    .build();
        }

        // Check if asset is in a warehouse (active placement only)
        Optional<AssetsOnWarehouse> warehouseLocation = assetsOnWarehouseRepository.findActiveByAssetId(assetId);
        if (warehouseLocation.isPresent()) {
            AssetsOnWarehouse placement = warehouseLocation.get();
            return AssetLocationCheckDto.builder()
                    .isPlaced(true)
                    .locationType(LocationType.WAREHOUSE)
                    .locationId(placement.getWarehouse().getId())
                    .locationName(placement.getWarehouse().getWarehouseName())
                    .locationCode(placement.getWarehouse().getWarehouseCode())
                    .assetTagId(asset.getAssetTagId())
                    .build();
        }

        // Check if asset is in a datacenter (active placement only)
        Optional<AssetsOnDatacenter> datacenterLocation = assetsOnDatacenterRepository.findActiveByAssetId(assetId);
        if (datacenterLocation.isPresent()) {
            AssetsOnDatacenter placement = datacenterLocation.get();
            return AssetLocationCheckDto.builder()
                    .isPlaced(true)
                    .locationType(LocationType.DATACENTER)
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
        log.info("Marking asset {} as vacated from current location", assetId);

        // Validate asset exists
        assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.ASSET_NOT_FOUND + assetId));

        LocalDate vacatedDate = LocalDate.now();

        // Check and mark as vacated from site
        assetsOnSiteRepository.findActiveByAssetId(assetId).ifPresent(placement -> {
            log.info("Marking asset {} as vacated from site {} on {}", 
                    assetId, placement.getSite().getSiteCode(), vacatedDate);
            placement.setVacatedOn(vacatedDate);
            assetsOnSiteRepository.save(placement);
        });

        // Check and mark as vacated from warehouse
        assetsOnWarehouseRepository.findActiveByAssetId(assetId).ifPresent(placement -> {
            log.info("Marking asset {} as vacated from warehouse {} on {}", 
                    assetId, placement.getWarehouse().getWarehouseCode(), vacatedDate);
            placement.setVacatedOn(vacatedDate);
            assetsOnWarehouseRepository.save(placement);
        });

        // Check and mark as vacated from datacenter
        assetsOnDatacenterRepository.findActiveByAssetId(assetId).ifPresent(placement -> {
            log.info("Marking asset {} as vacated from datacenter {} on {}", 
                    assetId, placement.getDatacenter().getDatacenterCode(), vacatedDate);
            placement.setVacatedOn(vacatedDate);
            assetsOnDatacenterRepository.save(placement);
        });

        log.info("Asset {} marked as vacated from current location successfully", assetId);
    }
}
