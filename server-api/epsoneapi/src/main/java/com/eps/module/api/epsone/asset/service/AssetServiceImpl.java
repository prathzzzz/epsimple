package com.eps.module.api.epsone.asset.service;

import com.eps.module.api.epsone.asset.dto.AssetBulkUploadDto;
import com.eps.module.api.epsone.asset.dto.AssetErrorReportDto;
import com.eps.module.api.epsone.asset.dto.AssetRequestDto;
import com.eps.module.api.epsone.asset.dto.AssetResponseDto;
import com.eps.module.api.epsone.asset.mapper.AssetMapper;
import com.eps.module.api.epsone.asset.processor.AssetBulkUploadProcessor;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnDatacenterRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnWarehouseRepository;
import com.eps.module.asset.Asset;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl extends BaseBulkUploadService<AssetBulkUploadDto, Asset> implements AssetService {

    private static final String ASSET_NOT_FOUND_WITH_ID_MSG = "Asset not found with id: ";

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final AssetsOnSiteRepository assetsOnSiteRepository;
    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;
    private final AssetsOnDatacenterRepository assetsOnDatacenterRepository;
    private final AssetBulkUploadProcessor assetBulkUploadProcessor;

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<AssetBulkUploadDto, Asset> getProcessor() {
        return assetBulkUploadProcessor;
    }

    @Override
    public Class<AssetBulkUploadDto> getBulkUploadDtoClass() {
        return AssetBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Asset";
    }

    @Override
    public List<Asset> getAllEntitiesForExport() {
        return assetRepository.findAllForExport();
    }

    @Override
    public Function<Asset, AssetBulkUploadDto> getEntityToDtoMapper() {
        return asset -> {
            // Fetch placement information for this asset
            String locationCode = null;
            String placementStatusCode = null;
            String placementStatusName = null;
            String assignedOn = null;
            String deliveredOn = null;
            String deployedOn = null;
            String activatedOn = null;
            String commissionedOn = null;
            String decommissionedOn = null;
            String vacatedOn = null;
            String disposedOn = null;
            String scrappedOn = null;

            // Check Site placement
            var sitePlacement = assetsOnSiteRepository.findByAssetId(asset.getId());
            if (sitePlacement.isPresent()) {
                var placement = sitePlacement.get();
                locationCode = placement.getSite() != null ? placement.getSite().getSiteCode() : null;
                placementStatusCode = placement.getAssetStatus() != null ? placement.getAssetStatus().getStatusCode() : null;
                placementStatusName = placement.getAssetStatus() != null ? placement.getAssetStatus().getStatusName() : null;
                assignedOn = placement.getAssignedOn() != null ? placement.getAssignedOn().toString() : null;
                deliveredOn = placement.getDeliveredOn() != null ? placement.getDeliveredOn().toString() : null;
                deployedOn = placement.getDeployedOn() != null ? placement.getDeployedOn().toString() : null;
                activatedOn = placement.getActivatedOn() != null ? placement.getActivatedOn().toString() : null;
                decommissionedOn = placement.getDecommissionedOn() != null ? placement.getDecommissionedOn().toString() : null;
                vacatedOn = placement.getVacatedOn() != null ? placement.getVacatedOn().toString() : null;
            }

            // Check Datacenter placement if no site placement
            if (locationCode == null) {
                var datacenterPlacement = assetsOnDatacenterRepository.findByAssetId(asset.getId());
                if (datacenterPlacement.isPresent()) {
                    var placement = datacenterPlacement.get();
                    locationCode = placement.getDatacenter() != null ? placement.getDatacenter().getDatacenterCode() : null;
                    placementStatusCode = placement.getAssetStatus() != null ? placement.getAssetStatus().getStatusCode() : null;
                    placementStatusName = placement.getAssetStatus() != null ? placement.getAssetStatus().getStatusName() : null;
                    assignedOn = placement.getAssignedOn() != null ? placement.getAssignedOn().toString() : null;
                    deliveredOn = placement.getDeliveredOn() != null ? placement.getDeliveredOn().toString() : null;
                    commissionedOn = placement.getCommissionedOn() != null ? placement.getCommissionedOn().toString() : null;
                    vacatedOn = placement.getVacatedOn() != null ? placement.getVacatedOn().toString() : null;
                    disposedOn = placement.getDisposedOn() != null ? placement.getDisposedOn().toString() : null;
                    scrappedOn = placement.getScrappedOn() != null ? placement.getScrappedOn().toString() : null;
                }
            }

            // Check Warehouse placement if no site or datacenter placement
            if (locationCode == null) {
                var warehousePlacement = assetsOnWarehouseRepository.findByAssetId(asset.getId());
                if (warehousePlacement.isPresent()) {
                    var placement = warehousePlacement.get();
                    locationCode = placement.getWarehouse() != null ? placement.getWarehouse().getWarehouseCode() : null;
                    placementStatusCode = placement.getAssetStatus() != null ? placement.getAssetStatus().getStatusCode() : null;
                    placementStatusName = placement.getAssetStatus() != null ? placement.getAssetStatus().getStatusName() : null;
                    assignedOn = placement.getAssignedOn() != null ? placement.getAssignedOn().toString() : null;
                    deliveredOn = placement.getDeliveredOn() != null ? placement.getDeliveredOn().toString() : null;
                    commissionedOn = placement.getCommissionedOn() != null ? placement.getCommissionedOn().toString() : null;
                    vacatedOn = placement.getVacatedOn() != null ? placement.getVacatedOn().toString() : null;
                    disposedOn = placement.getDisposedOn() != null ? placement.getDisposedOn().toString() : null;
                    scrappedOn = placement.getScrappedOn() != null ? placement.getScrappedOn().toString() : null;
                }
            }

            return AssetBulkUploadDto.builder()
                    .assetTagId(asset.getAssetTagId())
                    .assetName(asset.getAssetName())
                    .assetTypeName(asset.getAssetType() != null ? asset.getAssetType().getTypeName() : null)
                    .assetTypeCode(asset.getAssetType() != null ? asset.getAssetType().getTypeCode() : null)
                    .assetCategoryName(asset.getAssetCategory() != null ? asset.getAssetCategory().getCategoryName() : null)
                    .assetCategoryCode(asset.getAssetCategory() != null ? asset.getAssetCategory().getCategoryCode() : null)
                    .serialNumber(asset.getSerialNumber())
                    .modelNumber(asset.getModelNumber())
                    .vendorCode(asset.getVendor() != null ? asset.getVendor().getVendorCodeAlt() : null)
                    .vendorName(asset.getVendor() != null && asset.getVendor().getVendorDetails() != null ? 
                            buildVendorName(asset.getVendor().getVendorDetails()) : null)
                    .lenderBankName(asset.getLenderBank() != null ? asset.getLenderBank().getBankName() : null)
                    .lenderBankCode(asset.getLenderBank() != null ? asset.getLenderBank().getBankCodeAlt() : null)
                    .purchaseOrderNumber(asset.getPurchaseOrderNumber())
                    .purchaseOrderDate(asset.getPurchaseOrderDate() != null ? asset.getPurchaseOrderDate().toString() : null)
                    .purchaseOrderCost(asset.getPurchaseOrderCost() != null ? asset.getPurchaseOrderCost().toString() : null)
                    .dispatchOrderNumber(asset.getDispatchOrderNumber())
                    .dispatchOrderDate(asset.getDispatchOrderDate() != null ? asset.getDispatchOrderDate().toString() : null)
                    .warrantyPeriod(asset.getWarrantyPeriod() != null ? asset.getWarrantyPeriod().toString() : null)
                    .warrantyExpiryDate(asset.getWarrantyExpiryDate() != null ? asset.getWarrantyExpiryDate().toString() : null)
                    .endOfLifeDate(asset.getEndOfLifeDate() != null ? asset.getEndOfLifeDate().toString() : null)
                    .endOfSupportDate(asset.getEndOfSupportDate() != null ? asset.getEndOfSupportDate().toString() : null)
                    .statusCode(asset.getStatusType() != null ? asset.getStatusType().getStatusCode() : null)
                    .statusName(asset.getStatusType() != null ? asset.getStatusType().getStatusName() : null)
                    // Placement information
                    .locationCode(locationCode)
                    .placementStatusCode(placementStatusCode)
                    .placementStatusName(placementStatusName)
                    .assignedOn(assignedOn)
                    .deliveredOn(deliveredOn)
                    .deployedOn(deployedOn)
                    .activatedOn(activatedOn)
                    .commissionedOn(commissionedOn)
                    .decommissionedOn(decommissionedOn)
                    .vacatedOn(vacatedOn)
                    .disposedOn(disposedOn)
                    .scrappedOn(scrappedOn)
                    .build();
        };
    }

    @Override
    protected AssetErrorReportDto buildErrorReportDto(BulkUploadErrorDto error) {
        return AssetErrorReportDto.builder()
                .rowNumber(error.getRowNumber())
                .errorType(error.getErrorType())
                .errorMessage(error.getErrorMessage())
                .assetTagId(error.getRowData() != null ? (String) error.getRowData().get("assetTagId") : null)
                .assetName(error.getRowData() != null ? (String) error.getRowData().get("assetName") : null)
                .assetTypeName(error.getRowData() != null ? (String) error.getRowData().get("assetTypeName") : null)
                .assetCategoryName(error.getRowData() != null ? (String) error.getRowData().get("assetCategoryName") : null)
                .serialNumber(error.getRowData() != null ? (String) error.getRowData().get("serialNumber") : null)
                .modelNumber(error.getRowData() != null ? (String) error.getRowData().get("modelNumber") : null)
                .vendorCode(error.getRowData() != null ? (String) error.getRowData().get("vendorCode") : null)
                .lenderBankName(error.getRowData() != null ? (String) error.getRowData().get("lenderBankName") : null)
                .purchaseOrderNumber(error.getRowData() != null ? (String) error.getRowData().get("purchaseOrderNumber") : null)
                .purchaseOrderDate(error.getRowData() != null ? (String) error.getRowData().get("purchaseOrderDate") : null)
                .purchaseOrderCost(error.getRowData() != null ? (String) error.getRowData().get("purchaseOrderCost") : null)
                .dispatchOrderNumber(error.getRowData() != null ? (String) error.getRowData().get("dispatchOrderNumber") : null)
                .dispatchOrderDate(error.getRowData() != null ? (String) error.getRowData().get("dispatchOrderDate") : null)
                .warrantyPeriod(error.getRowData() != null ? (String) error.getRowData().get("warrantyPeriod") : null)
                .warrantyExpiryDate(error.getRowData() != null ? (String) error.getRowData().get("warrantyExpiryDate") : null)
                .endOfLifeDate(error.getRowData() != null ? (String) error.getRowData().get("endOfLifeDate") : null)
                .endOfSupportDate(error.getRowData() != null ? (String) error.getRowData().get("endOfSupportDate") : null)
                .statusCode(error.getRowData() != null ? (String) error.getRowData().get("statusCode") : null)
                .locationCode(error.getRowData() != null ? (String) error.getRowData().get("locationCode") : null)
                .placementStatusCode(error.getRowData() != null ? (String) error.getRowData().get("placementStatusCode") : null)
                .assignedOn(error.getRowData() != null ? (String) error.getRowData().get("assignedOn") : null)
                .deliveredOn(error.getRowData() != null ? (String) error.getRowData().get("deliveredOn") : null)
                .deployedOn(error.getRowData() != null ? (String) error.getRowData().get("deployedOn") : null)
                .activatedOn(error.getRowData() != null ? (String) error.getRowData().get("activatedOn") : null)
                .commissionedOn(error.getRowData() != null ? (String) error.getRowData().get("commissionedOn") : null)
                .decommissionedOn(error.getRowData() != null ? (String) error.getRowData().get("decommissionedOn") : null)
                .vacatedOn(error.getRowData() != null ? (String) error.getRowData().get("vacatedOn") : null)
                .disposedOn(error.getRowData() != null ? (String) error.getRowData().get("disposedOn") : null)
                .scrappedOn(error.getRowData() != null ? (String) error.getRowData().get("scrappedOn") : null)
                .build();
    }

    @Override
    public Class<AssetErrorReportDto> getErrorReportDtoClass() {
        return AssetErrorReportDto.class;
    }

    // Helper method to build vendor name from PersonDetails
    private String buildVendorName(com.eps.module.person.PersonDetails details) {
        if (details == null) {
            return null;
        }

        StringBuilder name = new StringBuilder();

        if (details.getFirstName() != null && !details.getFirstName().isEmpty()) {
            name.append(details.getFirstName());
        }

        if (details.getMiddleName() != null && !details.getMiddleName().isEmpty()) {
            if (name.length() > 0) name.append(" ");
            name.append(details.getMiddleName());
        }

        if (details.getLastName() != null && !details.getLastName().isEmpty()) {
            if (name.length() > 0) name.append(" ");
            name.append(details.getLastName());
        }

        return name.length() > 0 ? name.toString() : null;
    }

    // ========== Existing CRUD Methods ==========

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
                .orElseThrow(() -> new ResourceNotFoundException(ASSET_NOT_FOUND_WITH_ID_MSG + id));
        return assetMapper.toDto(asset);
    }

    @Override
    @Transactional
    public AssetResponseDto updateAsset(Long id, AssetRequestDto requestDto) {
        Asset existingAsset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ASSET_NOT_FOUND_WITH_ID_MSG + id));

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
                .orElseThrow(() -> new ResourceNotFoundException(ASSET_NOT_FOUND_WITH_ID_MSG + id));

        // Check for placements on sites
        long siteCount = assetsOnSiteRepository.countByAssetId(id);
        if (siteCount > 0) {
            throw new IllegalStateException(String.format(
                    "Cannot delete asset '%s' because it is placed on %d site%s. Please remove the asset from these sites first.",
                    asset.getAssetTagId(), siteCount, siteCount > 1 ? "s" : ""
            ));
        }

        // Check for placements in warehouses
        long warehouseCount = assetsOnWarehouseRepository.countByAssetId(id);
        if (warehouseCount > 0) {
            throw new IllegalStateException(String.format(
                    "Cannot delete asset '%s' because it is placed in %d warehouse%s. Please remove the asset from these warehouses first.",
                    asset.getAssetTagId(), warehouseCount, warehouseCount > 1 ? "s" : ""
            ));
        }

        // Check for placements in datacenters
        long datacenterCount = assetsOnDatacenterRepository.countByAssetId(id);
        if (datacenterCount > 0) {
            throw new IllegalStateException(String.format(
                    "Cannot delete asset '%s' because it is placed in %d datacenter%s. Please remove the asset from these datacenters first.",
                    asset.getAssetTagId(), datacenterCount, datacenterCount > 1 ? "s" : ""
            ));
        }

        assetRepository.deleteById(id);
    }
}
