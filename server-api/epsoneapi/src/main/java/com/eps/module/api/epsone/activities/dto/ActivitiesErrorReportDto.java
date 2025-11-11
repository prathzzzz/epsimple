package com.eps.module.api.epsone.activities.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitiesErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Activity Name (Master)", order = 2)
    private String masterActivityName;

    @ExcelColumn(value = "Activity Name", order = 3)
    private String activityName;

    @ExcelColumn(value = "Activity Category", order = 4)
    private String activityCategory;

    @ExcelColumn(value = "Activity Description", order = 5)
    private String activityDescription;

    @ExcelColumn(value = "Error", order = 6)
    private String error;
}
