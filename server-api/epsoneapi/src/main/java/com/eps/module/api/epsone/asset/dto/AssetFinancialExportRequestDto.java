package com.eps.module.api.epsone.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetFinancialExportRequestDto {
    private Long managedProjectId;
    private Long assetCategoryId;
    
    // WDV calculation: Tech Live Date -> wdvToDate
    private LocalDate wdvToDate;
    
    // Custom depreciation calculation: customFromDate -> customToDate
    private LocalDate customFromDate;
    private LocalDate customToDate;
}
