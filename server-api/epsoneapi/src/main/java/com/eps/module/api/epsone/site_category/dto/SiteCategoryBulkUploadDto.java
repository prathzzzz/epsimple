package com.eps.module.api.epsone.site_category.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteCategoryBulkUploadDto {

    @ExcelColumn(value = "Category Name", order = 1, example = "Data Center")
    private String categoryName;

    @ExcelColumn(value = "Category Code", order = 2, example = "DC")
    private String categoryCode;

    @ExcelColumn(value = "Description", order = 3, example = "Sites that house data center infrastructure")
    private String description;
}
