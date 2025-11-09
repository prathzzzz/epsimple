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
public class GenericStatusTypeBulkUploadDto {

    @ExcelColumn(value = "Status Name", order = 1, example = "Active")
    private String statusName;

    @ExcelColumn(value = "Status Code", order = 2, example = "ACTIVE")
    private String statusCode;

    @ExcelColumn(value = "Description", order = 3, example = "Status indicating active state")
    private String description;
}
