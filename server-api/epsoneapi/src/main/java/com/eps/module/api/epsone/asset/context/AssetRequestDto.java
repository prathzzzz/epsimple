package com.eps.module.api.epsone.asset.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequestDto {
    private String assetTagId;
    private Long assetTypeId;
    private Long assetCategoryId;
    private Long vendorId;
    private Long lenderBankId;
    private Long statusTypeId;
    private Long ownershipStatusId;
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
}
