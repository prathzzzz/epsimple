package com.eps.module.api.epsone.managed_project.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagedProjectErrorReportDto {

    @ExcelColumn(value = "Row Number")
    private Integer rowNumber;

    @ExcelColumn(value = "Error Type")
    private String errorType;

    @ExcelColumn(value = "Error Message")
    private String errorMessage;

    @ExcelColumn(value = "Project Name")
    private String projectName;

    @ExcelColumn(value = "Project Code")
    private String projectCode;

    @ExcelColumn(value = "Project Type")
    private String projectType;

    @ExcelColumn(value = "Project Description")
    private String projectDescription;

    @ExcelColumn(value = "Bank Name")
    private String bankName;
}
