package com.eps.module.api.epsone.activity.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Activity Name", order = 2)
    private String activityName;

    @ExcelColumn(value = "Activity Description", order = 3)
    private String activityDescription;

    @ExcelColumn(value = "Error", order = 4)
    private String error;
}
