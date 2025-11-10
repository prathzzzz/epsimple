package com.eps.module.api.epsone.asset_category.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetCategoryBulkUploadDto {

    @ExcelColumn(value = "Category Name", order = 1, example = "Automated Teller Machine")
    private String categoryName;

    @ExcelColumn(value = "Category Code", order = 2, example = "ATM")
    private String categoryCode;

    @ExcelColumn(value = "Asset Type Code", order = 3, example = "ATM")
    private String assetTypeCode;

    @ExcelColumn(value = "Asset Code Alt", order = 4, example = "ATM")
    private String assetCodeAlt;

    @ExcelColumn(value = "Description", order = 5, example = "Cash Dispensing Machine")
    private String description;
}
