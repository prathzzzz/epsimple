package com.eps.module.api.epsone.asset_placement.service;

import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_placement.constants.ErrorMessages;
import com.eps.module.api.epsone.asset_placement.dto.AssetLocationCheckDto;
import com.eps.module.api.epsone.asset_placement.dto.AssetPlacementBulkUploadDto;
import com.eps.module.api.epsone.asset_placement.processor.AssetPlacementBulkUploadProcessor;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnDatacenterRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnSiteRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnWarehouseRepository;
import com.eps.module.api.epsone.asset_movement.constants.LocationType;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetsOnDatacenter;
import com.eps.module.asset.AssetsOnSite;
import com.eps.module.asset.AssetsOnWarehouse;
import com.eps.module.common.bulk.excel.ExcelExportUtil;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.common.bulk.excel.ExcelImportUtil;
import com.eps.module.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetLocationServiceImpl implements AssetLocationService {

    private final AssetsOnSiteRepository assetsOnSiteRepository;
    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;
    private final AssetsOnDatacenterRepository assetsOnDatacenterRepository;
    private final AssetRepository assetRepository;
    private final AssetPlacementBulkUploadProcessor bulkUploadProcessor;
    private final ExcelImportUtil excelImportUtil;
    private final ExcelExportUtil excelExportUtil;

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

    @Override
    public SseEmitter bulkUploadPlacements(MultipartFile file) throws IOException {
        log.info("Starting asset placement bulk upload");
        
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            throw new IllegalArgumentException("Invalid file format. Please upload an Excel file (.xlsx)");
        }
        
        // Parse Excel file
        List<AssetPlacementBulkUploadDto> uploadData = excelImportUtil.parseExcelFile(file, AssetPlacementBulkUploadDto.class);
        
        log.info("Parsed {} placement records from Excel file", uploadData.size());
        
        // Create SSE emitter
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        // Add completion and error handlers
        emitter.onCompletion(() -> log.info("SSE emitter completed successfully for asset placement bulk upload"));
        emitter.onTimeout(() -> log.warn("SSE emitter timeout for asset placement bulk upload"));
        emitter.onError((ex) -> log.error("SSE emitter error for asset placement bulk upload: {}", ex.getMessage()));
        
        // Process async
        bulkUploadProcessor.processBulkUpload(uploadData, emitter);
        
        log.info("Asset placement bulk upload processing started asynchronously");
        
        return emitter;
    }

    @Override
    public ResponseEntity<byte[]> exportPlacementTemplate() throws IOException {
        log.info("Exporting asset placement template");
        
        // Create empty template with sample data
        List<AssetPlacementBulkUploadDto> sampleData = new ArrayList<>();
        sampleData.add(AssetPlacementBulkUploadDto.builder()
                .assetTagId("EXAMPLE-TAG-001")
                .locationCode("SITE-CODE")
                .placementStatusCode("ACTIVE")
                .assignedOn("")
                .deliveredOn("")
                .deployedOn("")
                .activatedOn("")
                .commissionedOn("")
                .decommissionedOn("")
                .vacatedOn("")
                .disposedOn("")
                .scrappedOn("")
                .build());
        
        byte[] excelData = excelExportUtil.exportToExcel(
                sampleData,
                AssetPlacementBulkUploadDto.class,
                "Asset_Placement_Template"
        );
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "Asset_Placement_Template_" + timestamp + ".xlsx";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }
}
