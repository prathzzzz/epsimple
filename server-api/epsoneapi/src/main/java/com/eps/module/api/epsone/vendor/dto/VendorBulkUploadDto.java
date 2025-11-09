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
public class VendorBulkUploadDto {

    @ExcelColumn(value = "Contact Number", order = 1, example = "9876543210")
    private String contactNumber;

    @ExcelColumn(value = "Vendor Type Name", order = 2, example = "Supplier")
    private String vendorTypeName;

    @ExcelColumn(value = "Vendor Code", order = 3, example = "VEN001")
    private String vendorCodeAlt;
}
