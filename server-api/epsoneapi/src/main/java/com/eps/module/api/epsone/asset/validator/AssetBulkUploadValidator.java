package com.eps.module.api.epsone.asset.validator;

import com.eps.module.api.epsone.asset.constant.AssetErrorMessages;
import com.eps.module.api.epsone.asset.dto.AssetBulkUploadDto;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_category.repository.AssetCategoryRepository;
import com.eps.module.api.epsone.asset_type.repository.AssetTypeRepository;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.api.epsone.data_center.repository.DatacenterRepository;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.api.epsone.warehouse.repository.WarehouseRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssetBulkUploadValidator implements BulkRowValidator<AssetBulkUploadDto> {

    private final AssetRepository assetRepository;
    private final AssetTypeRepository assetTypeRepository;
    private final AssetCategoryRepository assetCategoryRepository;
    private final VendorRepository vendorRepository;
    private final BankRepository bankRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final SiteRepository siteRepository;
    private final DatacenterRepository datacenterRepository;
    private final WarehouseRepository warehouseRepository;

    private static final Pattern ASSET_TAG_PATTERN = Pattern.compile("^[A-Z0-9]{5,50}$");
    private static final DateTimeFormatter[] ACCEPTED_DATE_FORMATTERS = new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd")
    };

    @Override
    public List<BulkUploadErrorDto> validate(AssetBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        log.debug("=== Validating row {} - Asset Tag: '{}', Type: '{}', Category: '{}'", 
                rowNumber, rowData.getAssetTagId(), rowData.getAssetTypeName(), rowData.getAssetCategoryName());

        // Validate Asset Tag ID (optional - will be auto-generated if not provided)
        if (!isBlank(rowData.getAssetTagId())) {
            String assetTagId = rowData.getAssetTagId().trim();
            if (assetTagId.length() < 5 || assetTagId.length() > 50) {
                errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                        AssetErrorMessages.ASSET_TAG_LENGTH_INVALID));
            } else if (!ASSET_TAG_PATTERN.matcher(assetTagId).matches()) {
                errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                        AssetErrorMessages.ASSET_TAG_FORMAT_INVALID));
            } else if (assetRepository.findByAssetTagId(assetTagId).isPresent()) {
                errors.add(createError(rowNumber, "DUPLICATE_ERROR", 
                        AssetErrorMessages.ASSET_TAG_ALREADY_EXISTS + assetTagId));
            }
        } else {
            log.debug("No asset tag ID provided - will be auto-generated");
        }

        // Validate Asset Type (required)
        if (isBlank(rowData.getAssetTypeName())) {
            errors.add(createError(rowNumber, "VALIDATION_ERROR", AssetErrorMessages.ASSET_TYPE_REQUIRED));
        } else if (!assetTypeRepository.findByTypeNameIgnoreCase(rowData.getAssetTypeName()).isPresent()) {
            errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                    AssetErrorMessages.ASSET_TYPE_NOT_FOUND + rowData.getAssetTypeName()));
        }

        // Validate Asset Category (required)
        if (isBlank(rowData.getAssetCategoryName())) {
            errors.add(createError(rowNumber, "VALIDATION_ERROR", AssetErrorMessages.ASSET_CATEGORY_REQUIRED));
        } else if (!assetCategoryRepository.findByCategoryNameIgnoreCase(rowData.getAssetCategoryName()).isPresent()) {
            errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                    AssetErrorMessages.ASSET_CATEGORY_NOT_FOUND + rowData.getAssetCategoryName()));
        }

        // Validate Vendor (required)
        if (isBlank(rowData.getVendorCode())) {
            errors.add(createError(rowNumber, "VALIDATION_ERROR", AssetErrorMessages.VENDOR_CODE_REQUIRED));
        } else if (!vendorRepository.existsByVendorCodeAlt(rowData.getVendorCode().trim())) {
            errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                    String.format(AssetErrorMessages.VENDOR_NOT_FOUND_CODE, rowData.getVendorCode())));
        }

        // Validate Lender Bank (required)
        if (isBlank(rowData.getLenderBankName())) {
            errors.add(createError(rowNumber, "VALIDATION_ERROR", AssetErrorMessages.LENDER_BANK_REQUIRED));
        } else if (!bankRepository.findByBankNameIgnoreCase(rowData.getLenderBankName()).isPresent()) {
            errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                    String.format(AssetErrorMessages.LENDER_BANK_NOT_FOUND, rowData.getLenderBankName())));
        }

        // Validate Status Code (optional)
        if (!isBlank(rowData.getStatusCode())) {
            if (!genericStatusTypeRepository.findByStatusCodeIgnoreCase(rowData.getStatusCode()).isPresent()) {
                errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                        String.format(AssetErrorMessages.STATUS_CODE_NOT_FOUND, rowData.getStatusCode())));
            }
        }

        // Validate Serial Number uniqueness (optional)
        if (!isBlank(rowData.getSerialNumber())) {
            if (assetRepository.findBySerialNumber(rowData.getSerialNumber().trim()).isPresent()) {
                errors.add(createError(rowNumber, "DUPLICATE_ERROR", 
                        AssetErrorMessages.SERIAL_NUMBER_ALREADY_EXISTS + rowData.getSerialNumber()));
            }
            validateLength(rowData.getSerialNumber(), "Serial Number", 100, rowNumber, errors);
        }

        // Validate string lengths
        validateLength(rowData.getAssetName(), "Asset Name", 255, rowNumber, errors);
        validateLength(rowData.getModelNumber(), "Model Number", 100, rowNumber, errors);
        validateLength(rowData.getPurchaseOrderNumber(), "Purchase Order Number", 100, rowNumber, errors);
        validateLength(rowData.getDispatchOrderNumber(), "Dispatch Order Number", 100, rowNumber, errors);

        // Validate dates (optional)
        validateDate(rowData.getPurchaseOrderDate(), "Purchase Order Date", rowNumber, errors);
        validateDate(rowData.getDispatchOrderDate(), "Dispatch Order Date", rowNumber, errors);
        validateDate(rowData.getWarrantyExpiryDate(), "Warranty Expiry Date", rowNumber, errors);
        validateDate(rowData.getEndOfLifeDate(), "End Of Life Date", rowNumber, errors);
        validateDate(rowData.getEndOfSupportDate(), "End Of Support Date", rowNumber, errors);

        // Validate Purchase Order Cost (optional)
        validateDecimal(rowData.getPurchaseOrderCost(), "Purchase Order Cost", rowNumber, errors);

        // Validate Warranty Period (optional)
        if (!isBlank(rowData.getWarrantyPeriod())) {
            try {
                Integer warrantyPeriod = Integer.parseInt(rowData.getWarrantyPeriod().trim());
                if (warrantyPeriod < 0) {
                    errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                            AssetErrorMessages.WARRANTY_PERIOD_NEGATIVE));
                }
            } catch (NumberFormatException e) {
                errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                        AssetErrorMessages.WARRANTY_PERIOD_INVALID));
            }
        }

        // Validate Placement Information (optional)
        // Location Code can be Site Code, Datacenter Code, or Warehouse Code
        if (!isBlank(rowData.getLocationCode())) {
            validatePlacement(rowData, rowNumber, errors);
        }

        return errors;
    }

    private void validatePlacement(AssetBulkUploadDto rowData, int rowNumber, List<BulkUploadErrorDto> errors) {
        String locationCode = rowData.getLocationCode().trim();
        
        // Try to find the location in order: Site -> Datacenter -> Warehouse
        boolean foundSite = siteRepository.existsBySiteCode(locationCode);
        boolean foundDatacenter = !foundSite && datacenterRepository.findByDatacenterCode(locationCode).isPresent();
        boolean foundWarehouse = !foundSite && !foundDatacenter && warehouseRepository.findByWarehouseCode(locationCode).isPresent();
        
        if (!foundSite && !foundDatacenter && !foundWarehouse) {
            errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                    String.format(AssetErrorMessages.LOCATION_CODE_NOT_FOUND, locationCode)));
            return;
        }

        // Validate placement-specific dates based on location type
        if (foundSite) {
            // SITE placement - validate Site-specific dates
            validateDate(rowData.getDeployedOn(), "Deployed On", rowNumber, errors);
            validateDate(rowData.getActivatedOn(), "Activated On", rowNumber, errors);
            validateDate(rowData.getDecommissionedOn(), "Decommissioned On", rowNumber, errors);
        } else if (foundDatacenter) {
            // DATACENTER placement - validate Datacenter-specific dates
            validateDate(rowData.getCommissionedOn(), "Commissioned On", rowNumber, errors);
            validateDate(rowData.getDisposedOn(), "Disposed On", rowNumber, errors);
            validateDate(rowData.getScrappedOn(), "Scrapped On", rowNumber, errors);
        } else if (foundWarehouse) {
            // WAREHOUSE placement - validate Warehouse-specific dates
            validateDate(rowData.getCommissionedOn(), "Commissioned On", rowNumber, errors);
            validateDate(rowData.getDisposedOn(), "Disposed On", rowNumber, errors);
            validateDate(rowData.getScrappedOn(), "Scrapped On", rowNumber, errors);
        }

        // Validate Placement Status (required when placement location is specified)
        if (isBlank(rowData.getPlacementStatusCode())) {
            errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                    AssetErrorMessages.PLACEMENT_STATUS_REQUIRED));
        } else if (!genericStatusTypeRepository.findByStatusCodeIgnoreCase(rowData.getPlacementStatusCode()).isPresent()) {
            errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                    String.format(AssetErrorMessages.PLACEMENT_STATUS_NOT_FOUND, rowData.getPlacementStatusCode())));
        }

        // Validate common placement dates
        validateDate(rowData.getAssignedOn(), "Assigned On", rowNumber, errors);
        validateDate(rowData.getDeliveredOn(), "Delivered On", rowNumber, errors);
        validateDate(rowData.getVacatedOn(), "Vacated On", rowNumber, errors);
    }

    @Override
    public boolean isDuplicate(AssetBulkUploadDto rowData) {
        if (!isBlank(rowData.getAssetTagId())) {
            return assetRepository.findByAssetTagId(rowData.getAssetTagId().trim()).isPresent();
        }
        // If no asset tag provided, it's not a duplicate (will be auto-generated)
        return false;
    }

    private void validateDate(String dateStr, String fieldName, int rowNumber, List<BulkUploadErrorDto> errors) {
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            String s = dateStr.trim();

            // Try the accepted formatters
            for (DateTimeFormatter fmt : ACCEPTED_DATE_FORMATTERS) {
                try {
                    LocalDate.parse(s, fmt);
                    return; // valid
                } catch (DateTimeParseException ignored) {
                }
            }

            // Accept Excel numeric date serials (e.g. 44927)
            if (s.matches("^\\d+(?:\\.\\d+)?$")) {
                try {
                    double serial = Double.parseDouble(s);
                    if (serial >= 1 && serial < 100000) {
                        return; // valid Excel serial number
                    }
                } catch (NumberFormatException ignored) {
                }
            }

            errors.add(createError(rowNumber, "VALIDATION_ERROR",
                    String.format(AssetErrorMessages.DATE_FORMAT_INVALID, fieldName)));
        }
    }

    private void validateDecimal(String value, String fieldName, int rowNumber, List<BulkUploadErrorDto> errors) {
        if (value != null && !value.trim().isEmpty()) {
            try {
                BigDecimal decimal = new BigDecimal(value.trim());
                if (decimal.compareTo(BigDecimal.ZERO) < 0) {
                    errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                            String.format(AssetErrorMessages.DECIMAL_NEGATIVE, fieldName)));
                }
            } catch (NumberFormatException e) {
                errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                        String.format(AssetErrorMessages.DECIMAL_INVALID, fieldName)));
            }
        }
    }

    private void validateLength(String value, String fieldName, int maxLength, int rowNumber, List<BulkUploadErrorDto> errors) {
        if (value != null && value.length() > maxLength) {
            errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                    String.format(AssetErrorMessages.LENGTH_EXCEEDED, fieldName, maxLength)));
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private BulkUploadErrorDto createError(int rowNumber, String errorType, String errorMessage) {
        return BulkUploadErrorDto.builder()
                .rowNumber(rowNumber)
                .errorType(errorType)
                .errorMessage(errorMessage)
                .build();
    }
}
