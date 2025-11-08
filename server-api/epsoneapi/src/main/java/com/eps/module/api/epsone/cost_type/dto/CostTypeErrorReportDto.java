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
public class CostTypeErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1, required = true)
    private Integer rowNumber;

    @ExcelColumn(value = "Error Type", order = 2, required = true)
    private String errorType;

    @ExcelColumn(value = "Error Message", order = 3, required = true)
    private String errorMessage;

    @ExcelColumn(value = "Type Name", order = 4)
    private String typeName;

    @ExcelColumn(value = "Type Description", order = 5)
    private String typeDescription;

    @ExcelColumn(value = "Cost Category Name", order = 6)
    private String costCategoryName;
}
