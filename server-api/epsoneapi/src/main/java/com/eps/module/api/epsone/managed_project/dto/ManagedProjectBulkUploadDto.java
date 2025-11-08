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
public class ManagedProjectBulkUploadDto {

    @ExcelColumn(value = "Project Name", order = 1, required = true, example = "Infrastructure Modernization")
    private String projectName;

    @ExcelColumn(value = "Project Code", order = 2, required = true, example = "PROJ-2024-001")
    private String projectCode;

    @ExcelColumn(value = "Project Type", order = 3, required = true, example = "IT Infrastructure")
    private String projectType;

    @ExcelColumn(value = "Project Description", order = 4, required = true, example = "Complete infrastructure modernization project")
    private String projectDescription;

    @ExcelColumn(value = "Bank Name", order = 5, required = true, example = "State Bank of India")
    private String bankName;

    // For export only - populated from relationship
    private String rbiBankCode;
    private String epsBankCode;
}
