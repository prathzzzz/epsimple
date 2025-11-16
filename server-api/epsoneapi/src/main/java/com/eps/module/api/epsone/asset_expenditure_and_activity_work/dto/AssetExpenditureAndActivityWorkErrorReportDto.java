package com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetExpenditureAndActivityWorkErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Asset Tag ID", order = 2)
    private String assetTagId;

    @ExcelColumn(value = "Invoice Number", order = 3)
    private String invoiceNumber;

    @ExcelColumn(value = "Activity Work ID", order = 4)
    private String activityWorkId;

    @ExcelColumn(value = "Error", order = 5)
    private String error;
}
