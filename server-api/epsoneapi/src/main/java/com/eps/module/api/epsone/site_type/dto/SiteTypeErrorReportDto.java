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
public class SiteTypeErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Type Name", order = 2)
    private String typeName;

    @ExcelColumn(value = "Description", order = 3)
    private String description;

    @ExcelColumn(value = "Error Message", order = 4)
    private String errorMessage;
}
