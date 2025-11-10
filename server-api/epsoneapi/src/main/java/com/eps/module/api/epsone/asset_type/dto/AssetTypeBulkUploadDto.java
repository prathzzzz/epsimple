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
public class AssetTypeBulkUploadDto {

    @ExcelColumn(value = "Type Name", order = 1, example = "Computer")
    private String typeName;

    @ExcelColumn(value = "Type Code", order = 2, example = "COMP")
    private String typeCode;

    @ExcelColumn(value = "Description", order = 3, example = "Computer equipment")
    private String description;
}
