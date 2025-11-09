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
public class VendorTypeBulkUploadDto {

    @ExcelColumn(value = "Type Name", order = 1, required = true, example = "Construction Contractor")
    private String typeName;

    @ExcelColumn(value = "Vendor Category Name", order = 2, required = true, example = "Construction")
    private String vendorCategoryName;

    @ExcelColumn(value = "Description", order = 3, required = false, example = "Vendors specializing in construction and building services")
    private String description;
}
