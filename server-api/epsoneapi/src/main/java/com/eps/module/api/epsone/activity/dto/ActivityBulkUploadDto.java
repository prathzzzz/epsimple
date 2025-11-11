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
public class ActivityBulkUploadDto {

    @ExcelColumn(value = "Activity Name", order = 1, required = true, example = "Site Preparation")
    private String activityName;

    @ExcelColumn(value = "Activity Description", order = 2, example = "Preparing the site for construction work")
    private String activityDescription;
}
