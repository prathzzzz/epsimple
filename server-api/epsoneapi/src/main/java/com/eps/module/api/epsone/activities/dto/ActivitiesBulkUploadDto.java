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
public class ActivitiesBulkUploadDto {

    @ExcelColumn(value = "Activity Name (Master)", order = 1, required = true, example = "Installation")
    private String masterActivityName;

    @ExcelColumn(value = "Activity Name", order = 2, required = true, example = "Server Installation")
    private String activityName;

    @ExcelColumn(value = "Activity Category", order = 3, example = "Hardware")
    private String activityCategory;

    @ExcelColumn(value = "Activity Description", order = 4, example = "Install and configure new server")
    private String activityDescription;
}
