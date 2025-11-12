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

    @ExcelColumn(value = "Project Name", order = 1, required = true, example = "BOI")
    private String projectName;

    @ExcelColumn(value = "Project Code", order = 2, required = true, example = "7BOI")
    private String projectCode;

    @ExcelColumn(value = "Project Type", order = 3, required = true, example = "BLA")
    private String projectType;

    @ExcelColumn(value = "Project Description", order = 4, required = true, example = "Bank of India ATM deployment and maintenance project")
    private String projectDescription;

    @ExcelColumn(value = "Bank Name", order = 5, required = true, example = "Bank of India")
    private String bankName;

    // For export only - populated from relationship
    private String rbiBankCode;
    private String epsBankCode;
}
