package com.eps.module.api.epsone.asset_placement.validator;

import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_placement.constants.AssetPlacementErrorMessages;
import com.eps.module.api.epsone.asset_placement.dto.AssetPlacementBulkUploadDto;
import com.eps.module.api.epsone.data_center.repository.DatacenterRepository;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.api.epsone.warehouse.repository.WarehouseRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssetPlacementBulkUploadValidator implements BulkRowValidator<AssetPlacementBulkUploadDto> {

    private final AssetRepository assetRepository;
    private final SiteRepository siteRepository;
    private final DatacenterRepository datacenterRepository;
    private final WarehouseRepository warehouseRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;

    @Override
    public List<BulkUploadErrorDto> validate(AssetPlacementBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Asset Tag ID (required)
        if (dto.getAssetTagId() == null || dto.getAssetTagId().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Asset Tag ID")
                    .errorMessage(AssetPlacementErrorMessages.ASSET_TAG_ID_REQUIRED)
                    .build());
        } else {
            // Check if asset exists
            if (!assetRepository.existsByAssetTagIdIgnoreCase(dto.getAssetTagId().trim())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Asset Tag ID")
                        .errorMessage(String.format(AssetPlacementErrorMessages.ASSET_TAG_NOT_FOUND, dto.getAssetTagId()))
                        .build());
            }
        }

        // Validate Location Code (required)
        if (dto.getLocationCode() == null || dto.getLocationCode().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Location Code")
                    .errorMessage(AssetPlacementErrorMessages.LOCATION_CODE_REQUIRED)
                    .build());
        } else {
            String locationCode = dto.getLocationCode().trim();
            // Check if location exists in any of the three types
            boolean locationExists = siteRepository.existsBySiteCode(locationCode) ||
                    datacenterRepository.findByDatacenterCode(locationCode).isPresent() ||
                    warehouseRepository.findByWarehouseCode(locationCode).isPresent();
            
            if (!locationExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Location Code")
                        .errorMessage(String.format(AssetPlacementErrorMessages.LOCATION_NOT_FOUND, locationCode))
                        .build());
            }
        }

        // Validate Placement Status Code (required)
        if (dto.getPlacementStatusCode() == null || dto.getPlacementStatusCode().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Placement Status Code")
                    .errorMessage(AssetPlacementErrorMessages.PLACEMENT_STATUS_CODE_REQUIRED)
                    .build());
        } else {
            if (!genericStatusTypeRepository.existsByStatusCode(dto.getPlacementStatusCode().trim())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Placement Status Code")
                        .errorMessage(String.format(AssetPlacementErrorMessages.PLACEMENT_STATUS_NOT_FOUND, dto.getPlacementStatusCode()))
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(AssetPlacementBulkUploadDto dto) {
        // For placement, we don't check for duplicates as an asset can have multiple placements over time
        // The processor will handle vacating old placements
        return false;
    }
}
