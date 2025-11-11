package com.eps.module.api.epsone.asset.processor;

import com.eps.module.api.epsone.asset.dto.AssetBulkUploadDto;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset.validator.AssetBulkUploadValidator;
import com.eps.module.api.epsone.asset_category.repository.AssetCategoryRepository;
import com.eps.module.api.epsone.asset_tag_code.service.AssetTagCodeGeneratorService;
import com.eps.module.api.epsone.asset_type.repository.AssetTypeRepository;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetCategory;
import com.eps.module.asset.AssetType;
import com.eps.module.bank.Bank;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.status.GenericStatusType;
import com.eps.module.vendor.Vendor;
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
    private final AssetTagCodeGeneratorService assetTagCodeGeneratorService;

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
            
            // Clean up thread-local storage
            currentDto.remove();
        } catch (Exception e) {
            // Clean up thread-local storage on error
            currentDto.remove();
            log.error("Error saving Asset entity: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving Asset entity: " + e.getMessage(), e);
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
