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
public class SiteCategoryErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Error Message", order = 2)
    private String errorMessage;

    @ExcelColumn(value = "Category Name", order = 3)
    private String categoryName;

    @ExcelColumn(value = "Category Code", order = 4)
    private String categoryCode;

    @ExcelColumn(value = "Description", order = 5)
    private String description;
}
