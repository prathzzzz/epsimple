package com.eps.module.api.epsone.asset.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetFinancialExportRowDto {

    @ExcelColumn(value = "Sr. No.", order = 1)
    private Integer serialNumber;

    @ExcelColumn(value = "Asset Tag ID", order = 2)
    private String assetTagId;

    @ExcelColumn(value = "Asset Name", order = 3)
    private String assetName;

    @ExcelColumn(value = "Asset Category", order = 4)
    private String assetCategoryName;

    @ExcelColumn(value = "Asset Type", order = 5)
    private String assetTypeName;

    @ExcelColumn(value = "Managed Project", order = 6)
    private String managedProjectName;

    @ExcelColumn(value = "Site Code", order = 7)
    private String siteCode;

    @ExcelColumn(value = "Site Name", order = 8)
    private String siteName;

    @ExcelColumn(value = "Tech Live Date", order = 9)
    private String techLiveDate;

    @ExcelColumn(value = "Revised Capital Value (₹)", order = 10)
    private BigDecimal revisedCapitalValue;

    @ExcelColumn(value = "Depreciation Rate (%)", order = 11)
    private Double depreciationPercentage;

    // WDV columns (from Tech Live Date to Selected WDV To Date)
    @ExcelColumn(value = "WDV To Date", order = 12)
    private String wdvToDate;

    @ExcelColumn(value = "WDV Duration (Days)", order = 13)
    private Long wdvDurationDays;

    @ExcelColumn(value = "WDV Duration (Years)", order = 14)
    private String wdvDurationYears;

    @ExcelColumn(value = "WDV Depreciation (₹)", order = 15)
    private BigDecimal wdvDepreciationAmount;

    @ExcelColumn(value = "WDV (₹)", order = 16)
    private BigDecimal writtenDownValue;

    // Custom depreciation columns (from Custom From Date to Custom To Date)
    @ExcelColumn(value = "Custom From Date", order = 17)
    private String customFromDate;

    @ExcelColumn(value = "Custom To Date", order = 18)
    private String customToDate;

    @ExcelColumn(value = "Custom Duration (Days)", order = 19)
    private Long customDurationDays;

    @ExcelColumn(value = "Custom Duration (Years)", order = 20)
    private String customDurationYears;

    @ExcelColumn(value = "Custom Depreciation (₹)", order = 21)
    private BigDecimal customDepreciationAmount;

    @ExcelColumn(value = "Status", order = 22)
    private String statusName;

    @ExcelColumn(value = "Is Scraped", order = 23)
    private String isScraped;

    @ExcelColumn(value = "Scraped Date", order = 24)
    private String scrapedDate;

    @ExcelColumn(value = "Loss Value (₹)", order = 25)
    private BigDecimal lossValue;
}
