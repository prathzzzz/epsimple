package com.eps.module.api.epsone.asset_placement.processor;

import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_movement.service.AssetMovementService;
import com.eps.module.api.epsone.asset_placement.dto.AssetPlacementBulkUploadDto;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnDatacenterRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnWarehouseRepository;
import com.eps.module.api.epsone.asset_placement.validator.AssetPlacementBulkUploadValidator;
import com.eps.module.api.epsone.data_center.repository.DatacenterRepository;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.movement_type.repository.MovementTypeRepository;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.api.epsone.warehouse.repository.WarehouseRepository;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetMovementType;
import com.eps.module.asset.AssetsOnDatacenter;
import com.eps.module.asset.AssetsOnSite;
import com.eps.module.asset.AssetsOnWarehouse;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.site.Site;
import com.eps.module.status.GenericStatusType;
import com.eps.module.warehouse.Datacenter;
import com.eps.module.warehouse.Warehouse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Processor for Asset Placement Bulk Upload
 * Handles creating and updating asset placements with automatic movement tracking
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AssetPlacementBulkUploadProcessor extends BulkUploadProcessor<AssetPlacementBulkUploadDto, AssetsOnSite> {

    private final AssetPlacementBulkUploadValidator validator;
    private final AssetRepository assetRepository;
    private final SiteRepository siteRepository;
    private final DatacenterRepository datacenterRepository;
    private final WarehouseRepository warehouseRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final AssetsOnSiteRepository assetsOnSiteRepository;
    private final AssetsOnDatacenterRepository assetsOnDatacenterRepository;
    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;
    private final AssetMovementService assetMovementService;
    private final MovementTypeRepository movementTypeRepository;

    // Thread-local storage for DTO data during processing
    private final ThreadLocal<AssetPlacementBulkUploadDto> currentDto = new ThreadLocal<>();

    private static final DateTimeFormatter[] ACCEPTED_DATE_FORMATTERS = new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd")
    };

    @Override
    protected BulkRowValidator<AssetPlacementBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected AssetsOnSite convertToEntity(AssetPlacementBulkUploadDto dto) {
        // Store DTO for later use in saveEntity
        currentDto.set(dto);
        
        // Return a dummy entity - actual placement creation happens in saveEntity
        return new AssetsOnSite();
    }

    @Override
    protected void saveEntity(AssetsOnSite entity) {
        try {
            AssetPlacementBulkUploadDto dto = currentDto.get();
            if (dto == null) {
                throw new RuntimeException("DTO not found in thread-local storage");
            }

            log.debug("Processing placement for asset: {}", dto.getAssetTagId());

            // Get the asset
            Asset asset = assetRepository.findByAssetTagId(dto.getAssetTagId().trim())
                    .orElseThrow(() -> new RuntimeException("Asset not found: " + dto.getAssetTagId()));

            String locationCode = dto.getLocationCode().trim();
            
            // Determine location type and create placement
            if (siteRepository.existsBySiteCode(locationCode)) {
                log.debug("Placing asset {} at Site: {}", asset.getAssetTagId(), locationCode);
                createSitePlacement(dto, asset, locationCode);
            } else if (datacenterRepository.findByDatacenterCode(locationCode).isPresent()) {
                log.debug("Placing asset {} at Datacenter: {}", asset.getAssetTagId(), locationCode);
                createDatacenterPlacement(dto, asset, locationCode);
            } else if (warehouseRepository.findByWarehouseCode(locationCode).isPresent()) {
                log.debug("Placing asset {} at Warehouse: {}", asset.getAssetTagId(), locationCode);
                createWarehousePlacement(dto, asset, locationCode);
            } else {
                throw new RuntimeException("Location code not found: " + locationCode);
            }

            log.debug("Successfully placed asset: {}", asset.getAssetTagId());

        } catch (Exception e) {
            log.error("Error processing placement: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing placement: " + e.getMessage(), e);
        } finally {
            // Clean up thread-local storage
            currentDto.remove();
        }
    }

    private void createSitePlacement(AssetPlacementBulkUploadDto dto, Asset asset, String siteCode) {
        Site site = siteRepository.findBySiteCode(siteCode)
                .orElseThrow(() -> new RuntimeException("Site not found: " + siteCode));

        GenericStatusType placementStatus = genericStatusTypeRepository.findByStatusCodeIgnoreCase(dto.getPlacementStatusCode())
                .orElseThrow(() -> new RuntimeException("Placement Status not found: " + dto.getPlacementStatusCode()));

        // Check for existing active placements and vacate them
        Object fromPlacement = null;
        String fromType = "Factory";
        
        // Check other locations and vacate if active
        Optional<AssetsOnWarehouse> activeWarehouse = assetsOnWarehouseRepository.findActiveByAssetId(asset.getId());
        if (activeWarehouse.isPresent()) {
            fromPlacement = activeWarehouse.get();
            fromType = "Warehouse";
            activeWarehouse.get().setVacatedOn(LocalDate.now());
            assetsOnWarehouseRepository.save(activeWarehouse.get());
            log.debug("Vacated existing Warehouse placement for Asset: {}", asset.getAssetTagId());
        }
        
        Optional<AssetsOnDatacenter> activeDatacenter = assetsOnDatacenterRepository.findActiveByAssetId(asset.getId());
        if (activeDatacenter.isPresent()) {
            fromPlacement = activeDatacenter.get();
            fromType = "Datacenter";
            activeDatacenter.get().setVacatedOn(LocalDate.now());
            assetsOnDatacenterRepository.save(activeDatacenter.get());
            log.debug("Vacated existing Datacenter placement for Asset: {}", asset.getAssetTagId());
        }
        
        // Check if already on a site (moving between sites)
        Optional<AssetsOnSite> activeSite = assetsOnSiteRepository.findActiveByAssetId(asset.getId());
        if (activeSite.isPresent()) {
            fromPlacement = activeSite.get();
            fromType = "Site";
            activeSite.get().setVacatedOn(LocalDate.now());
            assetsOnSiteRepository.save(activeSite.get());
            log.debug("Vacated existing Site placement for Asset: {}", asset.getAssetTagId());
        }

        // Create new placement
        AssetsOnSite assetsOnSite = AssetsOnSite.builder()
                .asset(asset)
                .site(site)
                .assetStatus(placementStatus)
                .assignedOn(parseDate(dto.getAssignedOn()))
                .deliveredOn(parseDate(dto.getDeliveredOn()))
                .deployedOn(parseDate(dto.getDeployedOn()))
                .activatedOn(parseDate(dto.getActivatedOn()))
                .decommissionedOn(parseDate(dto.getDecommissionedOn()))
                .vacatedOn(parseDate(dto.getVacatedOn()))
                .build();

        AssetsOnSite savedPlacement = assetsOnSiteRepository.save(assetsOnSite);
        log.debug("Created Site placement for Asset: {}", asset.getAssetTagId());

        // Track movement only if placement is active (vacatedOn is null)
        if (dto.getVacatedOn() == null || dto.getVacatedOn().trim().isEmpty()) {
            try {
                AssetMovementType movementType = movementTypeRepository.findByMovementType(fromType + " to Site")
                        .orElse(null);
                if (movementType != null) {
                    assetMovementService.trackMovement(asset, movementType, fromType.equals("Factory") ? "Factory" : null, fromPlacement, savedPlacement);
                    log.debug("Tracked movement for Asset: {} from {} to Site", asset.getAssetTagId(), fromType);
                }
            } catch (Exception e) {
                log.warn("Could not track movement for Asset {}: {}", asset.getAssetTagId(), e.getMessage());
            }
        }
    }

    private void createDatacenterPlacement(AssetPlacementBulkUploadDto dto, Asset asset, String datacenterCode) {
        Datacenter datacenter = datacenterRepository.findByDatacenterCode(datacenterCode)
                .orElseThrow(() -> new RuntimeException("Datacenter not found: " + datacenterCode));

        GenericStatusType placementStatus = genericStatusTypeRepository.findByStatusCodeIgnoreCase(dto.getPlacementStatusCode())
                .orElseThrow(() -> new RuntimeException("Placement Status not found: " + dto.getPlacementStatusCode()));

        // Check for existing active placements and vacate them
        Object fromPlacement = null;
        String fromType = "Factory";
        
        // Check other locations and vacate if active
        Optional<AssetsOnSite> activeSite = assetsOnSiteRepository.findActiveByAssetId(asset.getId());
        if (activeSite.isPresent()) {
            fromPlacement = activeSite.get();
            fromType = "Site";
            activeSite.get().setVacatedOn(LocalDate.now());
            assetsOnSiteRepository.save(activeSite.get());
            log.debug("Vacated existing Site placement for Asset: {}", asset.getAssetTagId());
        }
        
        Optional<AssetsOnWarehouse> activeWarehouse = assetsOnWarehouseRepository.findActiveByAssetId(asset.getId());
        if (activeWarehouse.isPresent()) {
            fromPlacement = activeWarehouse.get();
            fromType = "Warehouse";
            activeWarehouse.get().setVacatedOn(LocalDate.now());
            assetsOnWarehouseRepository.save(activeWarehouse.get());
            log.debug("Vacated existing Warehouse placement for Asset: {}", asset.getAssetTagId());
        }
        
        // Check if already in a datacenter (moving between datacenters)
        Optional<AssetsOnDatacenter> activeDatacenter = assetsOnDatacenterRepository.findActiveByAssetId(asset.getId());
        if (activeDatacenter.isPresent()) {
            fromPlacement = activeDatacenter.get();
            fromType = "Datacenter";
            activeDatacenter.get().setVacatedOn(LocalDate.now());
            assetsOnDatacenterRepository.save(activeDatacenter.get());
            log.debug("Vacated existing Datacenter placement for Asset: {}", asset.getAssetTagId());
        }

        // Create new placement
        AssetsOnDatacenter assetsOnDatacenter = AssetsOnDatacenter.builder()
                .asset(asset)
                .datacenter(datacenter)
                .assetStatus(placementStatus)
                .assignedOn(parseDate(dto.getAssignedOn()))
                .deliveredOn(parseDate(dto.getDeliveredOn()))
                .commissionedOn(parseDate(dto.getCommissionedOn()))
                .vacatedOn(parseDate(dto.getVacatedOn()))
                .disposedOn(parseDate(dto.getDisposedOn()))
                .scrappedOn(parseDate(dto.getScrappedOn()))
                .build();

        AssetsOnDatacenter savedPlacement = assetsOnDatacenterRepository.save(assetsOnDatacenter);
        log.debug("Created Datacenter placement for Asset: {}", asset.getAssetTagId());

        // Track movement only if placement is active (vacatedOn is null)
        if (dto.getVacatedOn() == null || dto.getVacatedOn().trim().isEmpty()) {
            try {
                AssetMovementType movementType = movementTypeRepository.findByMovementType(fromType + " to Datacenter")
                        .orElse(null);
                if (movementType != null) {
                    assetMovementService.trackMovement(asset, movementType, fromType.equals("Factory") ? "Factory" : null, fromPlacement, savedPlacement);
                    log.debug("Tracked movement for Asset: {} from {} to Datacenter", asset.getAssetTagId(), fromType);
                }
            } catch (Exception e) {
                log.warn("Could not track movement for Asset {}: {}", asset.getAssetTagId(), e.getMessage());
            }
        }
    }

    private void createWarehousePlacement(AssetPlacementBulkUploadDto dto, Asset asset, String warehouseCode) {
        Warehouse warehouse = warehouseRepository.findByWarehouseCode(warehouseCode)
                .orElseThrow(() -> new RuntimeException("Warehouse not found: " + warehouseCode));

        GenericStatusType placementStatus = genericStatusTypeRepository.findByStatusCodeIgnoreCase(dto.getPlacementStatusCode())
                .orElseThrow(() -> new RuntimeException("Placement Status not found: " + dto.getPlacementStatusCode()));

        // Check for existing active placements and vacate them
        Object fromPlacement = null;
        String fromType = "Factory";
        
        // Check other locations and vacate if active
        Optional<AssetsOnSite> activeSite = assetsOnSiteRepository.findActiveByAssetId(asset.getId());
        if (activeSite.isPresent()) {
            fromPlacement = activeSite.get();
            fromType = "Site";
            activeSite.get().setVacatedOn(LocalDate.now());
            assetsOnSiteRepository.save(activeSite.get());
            log.debug("Vacated existing Site placement for Asset: {}", asset.getAssetTagId());
        }
        
        Optional<AssetsOnDatacenter> activeDatacenter = assetsOnDatacenterRepository.findActiveByAssetId(asset.getId());
        if (activeDatacenter.isPresent()) {
            fromPlacement = activeDatacenter.get();
            fromType = "Datacenter";
            activeDatacenter.get().setVacatedOn(LocalDate.now());
            assetsOnDatacenterRepository.save(activeDatacenter.get());
            log.debug("Vacated existing Datacenter placement for Asset: {}", asset.getAssetTagId());
        }
        
        // Check if already in a warehouse (moving between warehouses)
        Optional<AssetsOnWarehouse> activeWarehouse = assetsOnWarehouseRepository.findActiveByAssetId(asset.getId());
        if (activeWarehouse.isPresent()) {
            fromPlacement = activeWarehouse.get();
            fromType = "Warehouse";
            activeWarehouse.get().setVacatedOn(LocalDate.now());
            assetsOnWarehouseRepository.save(activeWarehouse.get());
            log.debug("Vacated existing Warehouse placement for Asset: {}", asset.getAssetTagId());
        }

        // Create new placement
        AssetsOnWarehouse assetsOnWarehouse = AssetsOnWarehouse.builder()
                .asset(asset)
                .warehouse(warehouse)
                .assetStatus(placementStatus)
                .assignedOn(parseDate(dto.getAssignedOn()))
                .deliveredOn(parseDate(dto.getDeliveredOn()))
                .commissionedOn(parseDate(dto.getCommissionedOn()))
                .vacatedOn(parseDate(dto.getVacatedOn()))
                .disposedOn(parseDate(dto.getDisposedOn()))
                .scrappedOn(parseDate(dto.getScrappedOn()))
                .build();

        AssetsOnWarehouse savedPlacement = assetsOnWarehouseRepository.save(assetsOnWarehouse);
        log.debug("Created Warehouse placement for Asset: {}", asset.getAssetTagId());

        // Track movement only if placement is active (vacatedOn is null)
        if (dto.getVacatedOn() == null || dto.getVacatedOn().trim().isEmpty()) {
            try {
                AssetMovementType movementType = movementTypeRepository.findByMovementType(fromType + " to Warehouse")
                        .orElse(null);
                if (movementType != null) {
                    assetMovementService.trackMovement(asset, movementType, fromType.equals("Factory") ? "Factory" : null, fromPlacement, savedPlacement);
                    log.debug("Tracked movement for Asset: {} from {} to Warehouse", asset.getAssetTagId(), fromType);
                }
            } catch (Exception e) {
                log.warn("Could not track movement for Asset {}: {}", asset.getAssetTagId(), e.getMessage());
            }
        }
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(AssetPlacementBulkUploadDto dto) {
        Map<String, Object> data = new HashMap<>();
        data.put("assetTagId", dto.getAssetTagId());
        data.put("locationCode", dto.getLocationCode());
        data.put("placementStatusCode", dto.getPlacementStatusCode());
        data.put("assignedOn", dto.getAssignedOn());
        data.put("deliveredOn", dto.getDeliveredOn());
        data.put("deployedOn", dto.getDeployedOn());
        data.put("activatedOn", dto.getActivatedOn());
        data.put("commissionedOn", dto.getCommissionedOn());
        data.put("decommissionedOn", dto.getDecommissionedOn());
        data.put("vacatedOn", dto.getVacatedOn());
        data.put("disposedOn", dto.getDisposedOn());
        data.put("scrappedOn", dto.getScrappedOn());
        return data;
    }

    @Override
    protected boolean isEmptyRow(AssetPlacementBulkUploadDto dto) {
        return (dto.getAssetTagId() == null || dto.getAssetTagId().trim().isEmpty()) &&
               (dto.getLocationCode() == null || dto.getLocationCode().trim().isEmpty());
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        String s = dateStr.trim();

        // Try standard formatters
        for (DateTimeFormatter formatter : ACCEPTED_DATE_FORMATTERS) {
            try {
                return LocalDate.parse(s, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }

        // Try Excel serial number
        if (s.matches("^\\d+(?:\\.\\d+)?$")) {
            try {
                double serial = Double.parseDouble(s);
                LocalDate excelEpoch = LocalDate.of(1899, 12, 30);
                long days = (long) Math.floor(serial);
                return excelEpoch.plusDays(days);
            } catch (Exception ignored) {
            }
        }

        log.warn("Could not parse date: {}", dateStr);
        return null;
    }
}
