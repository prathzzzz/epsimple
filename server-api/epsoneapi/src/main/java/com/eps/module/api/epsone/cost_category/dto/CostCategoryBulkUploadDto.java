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

    @ExcelColumn(value = "Category Name", order = 1, required = true, example = "CAPEX")
    private String categoryName;

    @ExcelColumn(value = "Category Description", order = 2, required = true, example = "Capital Expenditure refers to the cost spent by a business to acquire, upgrade, or extend the life of a long term asset. It is a non recurring expenses.")
    private String categoryDescription;
}
