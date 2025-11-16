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
public class AssetExpenditureAndActivityWorkBulkUploadDto {

    @ExcelColumn(value = "Asset Tag ID", order = 1, required = true, example = "AST-001")
    private String assetTagId;

    @ExcelColumn(value = "Invoice Number", order = 2, example = "INV-2024-001")
    private String invoiceNumber;

    @ExcelColumn(value = "Activity Work ID", order = 3, example = "123")
    private String activityWorkId;
}
