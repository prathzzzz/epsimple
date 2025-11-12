package com.eps.module.api.epsone.cost_type.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostTypeBulkUploadDto {

    @ExcelColumn(value = "Type Name", order = 1, required = true, example = "Procurement")
    private String typeName;

    @ExcelColumn(value = "Type Description", order = 2, required = true, example = "The initial cost of acquiring the asset. Includes the purchase price and any applicable taxes, duties, or shipping costs.")
    private String typeDescription;

    @ExcelColumn(value = "Cost Category Name", order = 3, required = true, example = "CAPEX")
    private String costCategoryName;
}
