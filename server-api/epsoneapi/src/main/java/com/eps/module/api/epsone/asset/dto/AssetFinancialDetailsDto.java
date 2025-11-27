package com.eps.module.api.epsone.asset.dto;

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
public class AssetFinancialDetailsDto {
    // Asset Info
    private Long assetId;
    private String assetTagId;
    private String assetName;
    private String assetCategoryName;
    private String assetCategoryCode;
    private String assetTypeName;
    
    // Financial Info
    private BigDecimal purchaseOrderCost; // Revised Capital Value
    private Double depreciationPercentage; // From asset category
    
    // Site Info (if deployed)
    private Long siteId;
    private String siteCode;
    private String siteName;
    private LocalDate techLiveDate; // From site
    
    // Placement dates (from assets_on_site)
    private LocalDate assignedOn;
    private LocalDate deployedOn;
    private LocalDate activatedOn;
    private LocalDate decommissionedOn;
    private LocalDate vacatedOn;
    
    // Status
    private Long statusTypeId;
    private String statusTypeName;
}
