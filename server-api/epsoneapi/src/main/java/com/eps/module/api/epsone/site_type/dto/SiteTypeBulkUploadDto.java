package com.eps.module.api.epsone.site_type.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteTypeBulkUploadDto {

    @ExcelColumn(value = "Type Name", order = 1, example = "Office")
    private String typeName;

    @ExcelColumn(value = "Description", order = 2, example = "Office site type")
    private String description;
}
