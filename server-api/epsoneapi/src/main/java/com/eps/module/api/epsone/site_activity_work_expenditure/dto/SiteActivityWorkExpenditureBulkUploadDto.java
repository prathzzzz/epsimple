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
public class SiteActivityWorkExpenditureBulkUploadDto {

    @ExcelColumn(value = "Site Code", order = 1, required = true, example = "SITE-001")
    private String siteCode;

    @ExcelColumn(value = "Activity Name", order = 2, required = true, example = "Fiber Installation")
    private String activityName;

    @ExcelColumn(value = "Vendor Order Number", order = 3, required = true, example = "WO-2024-001")
    private String vendorOrderNumber;

    @ExcelColumn(value = "Invoice Number", order = 4, example = "INV-2024-001")
    private String invoiceNumber;
}
