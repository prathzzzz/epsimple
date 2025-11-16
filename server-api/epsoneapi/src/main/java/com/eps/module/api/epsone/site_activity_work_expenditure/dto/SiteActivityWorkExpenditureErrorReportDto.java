package com.eps.module.api.epsone.site_activity_work_expenditure.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteActivityWorkExpenditureErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Site Code", order = 2)
    private String siteCode;

    @ExcelColumn(value = "Activity Name", order = 3)
    private String activityName;

    @ExcelColumn(value = "Vendor Order Number", order = 4)
    private String vendorOrderNumber;

    @ExcelColumn(value = "Invoice Number", order = 5)
    private String invoiceNumber;

    @ExcelColumn(value = "Error Message", order = 6)
    private String errorMessage;
}
