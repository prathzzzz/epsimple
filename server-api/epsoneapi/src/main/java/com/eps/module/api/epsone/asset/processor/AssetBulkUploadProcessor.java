package com.eps.module.api.epsone.asset.processor;

import com.eps.module.api.epsone.asset.dto.AssetBulkUploadDto;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset.validator.AssetBulkUploadValidator;
import com.eps.module.api.epsone.asset_category.repository.AssetCategoryRepository;
import com.eps.module.api.epsone.asset_movement.service.AssetMovementService;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnDatacenterRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnWarehouseRepository;
import com.eps.module.api.epsone.asset_tag_code.service.AssetTagCodeGeneratorService;
import com.eps.module.api.epsone.asset_type.repository.AssetTypeRepository;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.api.epsone.data_center.repository.DatacenterRepository;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.movement_type.repository.MovementTypeRepository;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.api.epsone.warehouse.repository.WarehouseRepository;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetCategory;
import com.eps.module.asset.AssetMovementType;
import com.eps.module.asset.AssetType;
import com.eps.module.asset.AssetsOnDatacenter;
import com.eps.module.asset.AssetsOnSite;
import com.eps.module.asset.AssetsOnWarehouse;
import com.eps.module.bank.Bank;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.site.Site;
import com.eps.module.status.GenericStatusType;
import com.eps.module.vendor.Vendor;
import com.eps.module.warehouse.Datacenter;
import com.eps.module.warehouse.Warehouse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssetBulkUploadProcessor extends BulkUploadProcessor<AssetBulkUploadDto, Asset> {

    private final AssetBulkUploadValidator validator;
    private final AssetRepository assetRepository;
    private final AssetTypeRepository assetTypeRepository;
    private final AssetCategoryRepository assetCategoryRepository;
    private final VendorRepository vendorRepository;
    private final BankRepository bankRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final SiteRepository siteRepository;
    private final DatacenterRepository datacenterRepository;
    private final WarehouseRepository warehouseRepository;
    private final AssetsOnSiteRepository assetsOnSiteRepository;
    private final AssetsOnDatacenterRepository assetsOnDatacenterRepository;
    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;
    private final AssetTagCodeGeneratorService assetTagCodeGeneratorService;
    private final AssetMovementService assetMovementService;
    private final MovementTypeRepository movementTypeRepository;

    // Thread-local storage for DTO data during processing
    private final ThreadLocal<AssetBulkUploadDto> currentDto = new ThreadLocal<>();

    private static final DateTimeFormatter[] ACCEPTED_DATE_FORMATTERS = new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd")
    };

    @Override
    protected BulkRowValidator<AssetBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected Asset convertToEntity(AssetBulkUploadDto dto) {
        try {
            log.debug("Converting DTO to Asset entity: {}", dto.getAssetTagId());

            // Store DTO for later use in saveEntity
            currentDto.set(dto);

            Asset asset = new Asset();
            
            // Get required entities for potential auto-generation
            AssetCategory assetCategory = assetCategoryRepository.findByCategoryNameIgnoreCase(dto.getAssetCategoryName())
                    .orElseThrow(() -> new RuntimeException("Asset Category not found: " + dto.getAssetCategoryName()));
            
            Vendor vendor = vendorRepository.findByVendorCodeIgnoreCase(dto.getVendorCode())
                    .orElseThrow(() -> new RuntimeException("Vendor not found with code: " + dto.getVendorCode()));
            
            Bank lenderBank = bankRepository.findByBankNameIgnoreCase(dto.getLenderBankName())
                    .orElseThrow(() -> new RuntimeException("Bank not found: " + dto.getLenderBankName()));
            
            // Handle Asset Tag ID - auto-generate if not provided
            String assetTagId = dto.getAssetTagId();
            log.info("Processing asset tag - Provided: '{}', IsNull: {}, IsEmpty: {}", 
                    assetTagId, assetTagId == null, assetTagId != null && assetTagId.trim().isEmpty());
            
            if (assetTagId == null || assetTagId.trim().isEmpty()) {
                log.info("No asset tag ID provided - will auto-generate");
                try {
                    String generatedTag = assetTagCodeGeneratorService
                            .generateAssetTag(assetCategory.getId(), vendor.getId(), lenderBank.getId())
                            .getAssetTag();
                    
                    // Validate generated tag
                    if (generatedTag == null || generatedTag.length() < 5 || generatedTag.length() > 50) {
                        log.error("Generated asset tag '{}' is invalid (must be 5-50 characters). Category: {}, Vendor: {}, Bank: {}", 
                                generatedTag, assetCategory.getCategoryName(), vendor.getVendorCodeAlt(), lenderBank.getBankName());
                        throw new IllegalStateException("Generated asset tag is too short or too long");
                    } else if (!generatedTag.matches("^[A-Z0-9]+$")) {
                        log.error("Generated asset tag '{}' contains invalid characters (must be uppercase alphanumeric). Category: {}, Vendor: {}, Bank: {}", 
                                generatedTag, assetCategory.getCategoryName(), vendor.getVendorCodeAlt(), lenderBank.getBankName());
                        throw new IllegalStateException("Generated asset tag contains invalid characters");
                    } else {
                        asset.setAssetTagId(generatedTag);
                        log.info("Auto-generated asset tag: {} for category: {}, vendor: {}, bank: {}", 
                                generatedTag, assetCategory.getCategoryName(), vendor.getVendorCodeAlt(), lenderBank.getBankName());
                    }
                } catch (Exception e) {
                    log.error("Failed to auto-generate asset tag for category: {}, vendor: {}, bank: {}", 
                            dto.getAssetCategoryName(), dto.getVendorCode(), dto.getLenderBankName(), e);
                    throw new IllegalStateException("Cannot auto-generate asset tag: " + e.getMessage(), e);
                }
            } else {
                asset.setAssetTagId(assetTagId.trim());
                log.debug("Using provided asset tag: {}", assetTagId.trim());
            }
            
            if (dto.getAssetName() != null && !dto.getAssetName().trim().isEmpty()) {
                asset.setAssetName(dto.getAssetName().trim());
            }

            // Set Asset Type (required)
            AssetType assetType = assetTypeRepository.findByTypeNameIgnoreCase(dto.getAssetTypeName())
                    .orElseThrow(() -> new RuntimeException("Asset Type not found: " + dto.getAssetTypeName()));
            asset.setAssetType(assetType);

            // Set Asset Category (already fetched above)
            asset.setAssetCategory(assetCategory);

            // Set Vendor (already fetched above)
            asset.setVendor(vendor);

            // Set Lender Bank (already fetched above)
            asset.setLenderBank(lenderBank);

            // Set Status (optional)
            if (dto.getStatusCode() != null && !dto.getStatusCode().trim().isEmpty()) {
                GenericStatusType statusType = genericStatusTypeRepository.findByStatusCodeIgnoreCase(dto.getStatusCode())
                        .orElse(null);
                asset.setStatusType(statusType);
            }

            // Set optional fields
            if (dto.getSerialNumber() != null && !dto.getSerialNumber().trim().isEmpty()) {
                asset.setSerialNumber(dto.getSerialNumber().trim());
            }

            if (dto.getModelNumber() != null && !dto.getModelNumber().trim().isEmpty()) {
                asset.setModelNumber(dto.getModelNumber().trim());
            }

            if (dto.getPurchaseOrderNumber() != null && !dto.getPurchaseOrderNumber().trim().isEmpty()) {
                asset.setPurchaseOrderNumber(dto.getPurchaseOrderNumber().trim());
            }

            // Parse dates
            asset.setPurchaseOrderDate(parseDate(dto.getPurchaseOrderDate()));
            asset.setDispatchOrderDate(parseDate(dto.getDispatchOrderDate()));
            asset.setWarrantyExpiryDate(parseDate(dto.getWarrantyExpiryDate()));
            asset.setEndOfLifeDate(parseDate(dto.getEndOfLifeDate()));
            asset.setEndOfSupportDate(parseDate(dto.getEndOfSupportDate()));

            // Parse Purchase Order Cost
            if (dto.getPurchaseOrderCost() != null && !dto.getPurchaseOrderCost().trim().isEmpty()) {
                asset.setPurchaseOrderCost(new BigDecimal(dto.getPurchaseOrderCost().trim()));
            }

            // Parse Dispatch Order Number
            if (dto.getDispatchOrderNumber() != null && !dto.getDispatchOrderNumber().trim().isEmpty()) {
                asset.setDispatchOrderNumber(dto.getDispatchOrderNumber().trim());
            }

            // Parse Warranty Period
            if (dto.getWarrantyPeriod() != null && !dto.getWarrantyPeriod().trim().isEmpty()) {
                asset.setWarrantyPeriod(Integer.parseInt(dto.getWarrantyPeriod().trim()));
            }

            log.debug("Successfully converted DTO to Asset entity: {}", asset.getAssetTagId());
            return asset;

        } catch (Exception e) {
            log.error("Error converting DTO to Asset entity: {}", e.getMessage(), e);
            throw new RuntimeException("Error converting DTO to Asset entity: " + e.getMessage(), e);
        }
    }

    @Override
    protected void saveEntity(Asset entity) {
        try {
            log.debug("Saving Asset entity: {}", entity.getAssetTagId());
            Asset savedAsset = assetRepository.save(entity);
            log.debug("Successfully saved Asset entity: {}", savedAsset.getAssetTagId());

            // Handle asset placement if specified
            AssetBulkUploadDto dto = currentDto.get();
            if (dto != null && dto.getLocationCode() != null && !dto.getLocationCode().trim().isEmpty()) {
                try {
                    String locationCode = dto.getLocationCode().trim();
                    
                    // Try to find the location in order: Site -> Datacenter -> Warehouse
                    if (siteRepository.existsBySiteCode(locationCode)) {
                        log.debug("Creating SITE placement for Asset: {} at Site: {}", savedAsset.getAssetTagId(), locationCode);
                        createSitePlacement(dto, savedAsset, locationCode);
                    } else if (datacenterRepository.findByDatacenterCode(locationCode).isPresent()) {
                        log.debug("Creating DATACENTER placement for Asset: {} at Datacenter: {}", savedAsset.getAssetTagId(), locationCode);
                        createDatacenterPlacement(dto, savedAsset, locationCode);
                    } else if (warehouseRepository.findByWarehouseCode(locationCode).isPresent()) {
                        log.debug("Creating WAREHOUSE placement for Asset: {} at Warehouse: {}", savedAsset.getAssetTagId(), locationCode);
                        createWarehousePlacement(dto, savedAsset, locationCode);
                    } else {
                        log.warn("Location code '{}' not found for Asset: {}", locationCode, savedAsset.getAssetTagId());
                    }
                } catch (Exception e) {
                    log.error("Error creating placement for Asset {}: {}", savedAsset.getAssetTagId(), e.getMessage(), e);
                    // Don't throw exception - asset is already saved, just log the error
                } finally {
                    // Clean up thread-local storage
                    currentDto.remove();
                }
            } else {
                // Clean up thread-local storage even if no placement
                currentDto.remove();
            }
        } catch (Exception e) {
            // Clean up thread-local storage on error
            currentDto.remove();
            log.error("Error saving Asset entity: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving Asset entity: " + e.getMessage(), e);
        }
    }

    private void createSitePlacement(AssetBulkUploadDto dto, Asset asset, String siteCode) {
        Site site = siteRepository.findBySiteCode(siteCode)
                .orElseThrow(() -> new RuntimeException("Site not found: " + siteCode));

        GenericStatusType placementStatus = genericStatusTypeRepository.findByStatusCodeIgnoreCase(dto.getPlacementStatusCode())
                .orElseThrow(() -> new RuntimeException("Placement Status not found: " + dto.getPlacementStatusCode()));

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
                AssetMovementType movementType = movementTypeRepository.findByMovementType("Factory to Site")
                        .orElse(null);
                if (movementType != null) {
                    assetMovementService.trackMovement(asset, movementType, "Factory", null, savedPlacement);
                    log.debug("Tracked movement for Asset: {}", asset.getAssetTagId());
                }
            } catch (Exception e) {
                log.warn("Could not track movement for Asset {}: {}", asset.getAssetTagId(), e.getMessage());
            }
        }
    }

    private void createDatacenterPlacement(AssetBulkUploadDto dto, Asset asset, String datacenterCode) {
        Datacenter datacenter = datacenterRepository.findByDatacenterCode(datacenterCode)
                .orElseThrow(() -> new RuntimeException("Datacenter not found: " + datacenterCode));

        GenericStatusType placementStatus = genericStatusTypeRepository.findByStatusCodeIgnoreCase(dto.getPlacementStatusCode())
                .orElseThrow(() -> new RuntimeException("Placement Status not found: " + dto.getPlacementStatusCode()));

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
                AssetMovementType movementType = movementTypeRepository.findByMovementType("Factory to Datacenter")
                        .orElse(null);
                if (movementType != null) {
                    assetMovementService.trackMovement(asset, movementType, "Factory", null, savedPlacement);
                    log.debug("Tracked movement for Asset: {}", asset.getAssetTagId());
                }
            } catch (Exception e) {
                log.warn("Could not track movement for Asset {}: {}", asset.getAssetTagId(), e.getMessage());
            }
        }
    }

    private void createWarehousePlacement(AssetBulkUploadDto dto, Asset asset, String warehouseCode) {
        Warehouse warehouse = warehouseRepository.findByWarehouseCode(warehouseCode)
                .orElseThrow(() -> new RuntimeException("Warehouse not found: " + warehouseCode));

        GenericStatusType placementStatus = genericStatusTypeRepository.findByStatusCodeIgnoreCase(dto.getPlacementStatusCode())
                .orElseThrow(() -> new RuntimeException("Placement Status not found: " + dto.getPlacementStatusCode()));

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
                AssetMovementType movementType = movementTypeRepository.findByMovementType("Factory to Warehouse")
                        .orElse(null);
                if (movementType != null) {
                    assetMovementService.trackMovement(asset, movementType, "Factory", null, savedPlacement);
                    log.debug("Tracked movement for Asset: {}", asset.getAssetTagId());
                }
            } catch (Exception e) {
                log.warn("Could not track movement for Asset {}: {}", asset.getAssetTagId(), e.getMessage());
            }
        }
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(AssetBulkUploadDto dto) {
        Map<String, Object> data = new HashMap<>();
        data.put("assetTagId", dto.getAssetTagId());
        data.put("assetName", dto.getAssetName());
        data.put("assetTypeName", dto.getAssetTypeName());
        data.put("assetCategoryName", dto.getAssetCategoryName());
        data.put("serialNumber", dto.getSerialNumber());
        data.put("modelNumber", dto.getModelNumber());
        data.put("vendorCode", dto.getVendorCode());
        data.put("lenderBankName", dto.getLenderBankName());
        data.put("purchaseOrderNumber", dto.getPurchaseOrderNumber());
        data.put("purchaseOrderDate", dto.getPurchaseOrderDate());
        data.put("purchaseOrderCost", dto.getPurchaseOrderCost());
        data.put("dispatchOrderNumber", dto.getDispatchOrderNumber());
        data.put("dispatchOrderDate", dto.getDispatchOrderDate());
        data.put("warrantyPeriod", dto.getWarrantyPeriod());
        data.put("warrantyExpiryDate", dto.getWarrantyExpiryDate());
        data.put("endOfLifeDate", dto.getEndOfLifeDate());
        data.put("endOfSupportDate", dto.getEndOfSupportDate());
        data.put("statusCode", dto.getStatusCode());
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
    protected boolean isEmptyRow(AssetBulkUploadDto dto) {
        return (dto.getAssetTagId() == null || dto.getAssetTagId().trim().isEmpty()) &&
               (dto.getAssetTypeName() == null || dto.getAssetTypeName().trim().isEmpty()) &&
               (dto.getAssetCategoryName() == null || dto.getAssetCategoryName().trim().isEmpty()) &&
               (dto.getVendorCode() == null || dto.getVendorCode().trim().isEmpty()) &&
               (dto.getLenderBankName() == null || dto.getLenderBankName().trim().isEmpty());
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
