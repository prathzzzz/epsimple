package com.eps.module.api.epsone.cost_category.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostCategoryBulkUploadDto {

    @ExcelColumn(value = "Category Name", order = 1, required = true, example = "Operational Expenses")
    private String categoryName;

    @ExcelColumn(value = "Category Description", order = 2, required = true, example = "Expenses related to daily operations")
    private String categoryDescription;
}
