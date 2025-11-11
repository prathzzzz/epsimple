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
public class ActivityWorkErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Activities Name", order = 2)
    private String activitiesName;

    @ExcelColumn(value = "Vendor Name", order = 3)
    private String vendorName;

    @ExcelColumn(value = "Vendor Order Number", order = 4)
    private String vendorOrderNumber;

    @ExcelColumn(value = "Work Order Date", order = 5)
    private String workOrderDate;

    @ExcelColumn(value = "Work Start Date", order = 6)
    private String workStartDate;

    @ExcelColumn(value = "Work Completion Date", order = 7)
    private String workCompletionDate;

    @ExcelColumn(value = "Status Type Code", order = 8)
    private String statusTypeCode;

    @ExcelColumn(value = "Error", order = 9)
    private String error;
}
