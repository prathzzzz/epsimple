package com.eps.module.api.epsone.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetResponseDto {
    private Long id;
    private String assetTagId;

    // Asset Type
    private Long assetTypeId;
    private String assetTypeName;
    private String assetTypeCode;

    // Asset Category
    private Long assetCategoryId;
    private String assetCategoryName;
    private String assetCategoryCode;

    // Vendor
    private Long vendorId;
    private String vendorName;
    private String vendorCode;

    // Lender Bank
    private Long lenderBankId;
    private String lenderBankName;
    private String lenderBankCode;

    // Status Type
    private Long statusTypeId;
    private String statusTypeName;

    // Asset Details
    private String assetName;
    private String serialNumber;
    private String modelNumber;
    private String purchaseOrderNumber;
    private LocalDate purchaseOrderDate;
    private BigDecimal purchaseOrderCost;
    private String dispatchOrderNumber;
    private LocalDate dispatchOrderDate;
    private Integer warrantyPeriod;
    private LocalDate warrantyExpiryDate;
    private LocalDate endOfLifeDate;
    private LocalDate endOfSupportDate;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
