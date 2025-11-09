package com.eps.module.api.epsone.generic_status_type.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericStatusTypeErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Error Message", order = 2)
    private String errorMessage;

    @ExcelColumn(value = "Status Name", order = 3)
    private String statusName;

    @ExcelColumn(value = "Status Code", order = 4)
    private String statusCode;

    @ExcelColumn(value = "Description", order = 5)
    private String description;
}
