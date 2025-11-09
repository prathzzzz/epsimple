package com.eps.module.api.epsone.vendor_type.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorTypeErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1, required = true)
    private Integer rowNumber;

    @ExcelColumn(value = "Type Name", order = 2, required = false)
    private String typeName;

    @ExcelColumn(value = "Vendor Category Name", order = 3, required = false)
    private String vendorCategoryName;

    @ExcelColumn(value = "Description", order = 4, required = false)
    private String description;

    @ExcelColumn(value = "Error Type", order = 5, required = true)
    private String errorType;

    @ExcelColumn(value = "Error Message", order = 6, required = true)
    private String errorMessage;
}
