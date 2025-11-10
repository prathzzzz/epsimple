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
public class AssetCategoryErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Category Name", order = 2)
    private String categoryName;

    @ExcelColumn(value = "Category Code", order = 3)
    private String categoryCode;

    @ExcelColumn(value = "Asset Type Code", order = 4)
    private String assetTypeCode;

    @ExcelColumn(value = "Asset Code Alt", order = 5)
    private String assetCodeAlt;

    @ExcelColumn(value = "Description", order = 6)
    private String description;

    @ExcelColumn(value = "Error Message", order = 7)
    private String errorMessage;
}
