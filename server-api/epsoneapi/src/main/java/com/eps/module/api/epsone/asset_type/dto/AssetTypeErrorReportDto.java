package com.eps.module.api.epsone.asset_type.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetTypeErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Type Name", order = 2)
    private String typeName;

    @ExcelColumn(value = "Type Code", order = 3)
    private String typeCode;

    @ExcelColumn(value = "Description", order = 4)
    private String description;

    @ExcelColumn(value = "Error Message", order = 5)
    private String errorMessage;
}
