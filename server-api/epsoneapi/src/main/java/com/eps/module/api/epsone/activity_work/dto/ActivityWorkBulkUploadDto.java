package com.eps.module.api.epsone.activity_work.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityWorkBulkUploadDto {

    @ExcelColumn(value = "Activities Name", order = 1, required = true, example = "Server Installation")
    private String activitiesName;

    @ExcelColumn(value = "Vendor Name", order = 2, required = true, example = "Tech Solutions Inc")
    private String vendorName;

    @ExcelColumn(value = "Vendor Order Number", order = 3, example = "PO-2024-001")
    private String vendorOrderNumber;

    @ExcelColumn(value = "Work Order Date", order = 4, example = "2024-01-15")
    private String workOrderDate;

    @ExcelColumn(value = "Work Start Date", order = 5, example = "2024-01-20")
    private String workStartDate;

    @ExcelColumn(value = "Work Completion Date", order = 6, example = "2024-02-15")
    private String workCompletionDate;

    @ExcelColumn(value = "Status Type Code", order = 7, required = true, example = "IN_PROGRESS")
    private String statusTypeCode;
}
