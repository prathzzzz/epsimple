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

    @ExcelColumn(value = "Type Name", order = 1, required = true, example = "Hardware Purchase")
    private String typeName;

    @ExcelColumn(value = "Type Description", order = 2, required = true, example = "Purchase of hardware equipment")
    private String typeDescription;

    @ExcelColumn(value = "Cost Category Name", order = 3, required = true, example = "Operational Expenses")
    private String costCategoryName;
}
