package com.eps.module.api.epsone.vendor_category.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorCategoryBulkUploadDto {

    @ExcelColumn(value = "Category Name", order = 1, required = true, example = "Construction")
    private String categoryName;

    @ExcelColumn(value = "Description", order = 2, required = false, example = "Vendors who construct and set up the site for functioning, including building infrastructure, foundations, and structural work.")
    private String description;
}
