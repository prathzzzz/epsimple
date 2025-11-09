package com.eps.module.api.epsone.vendor.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Error Message", order = 2)
    private String errorMessage;

    @ExcelColumn(value = "Contact Number", order = 3)
    private String contactNumber;

    @ExcelColumn(value = "Vendor Type Name", order = 4)
    private String vendorTypeName;

    @ExcelColumn(value = "Vendor Code", order = 5)
    private String vendorCodeAlt;
}
