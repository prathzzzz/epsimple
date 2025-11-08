package com.eps.module.api.epsone.data_center.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatacenterBulkUploadDto {

    @ExcelColumn(value = "Datacenter Name", order = 1, required = true, example = "DC-Mumbai-01")
    private String datacenterName;

    @ExcelColumn(value = "Datacenter Code", order = 2, required = true, example = "DC-MUM-01")
    private String datacenterCode;

    @ExcelColumn(value = "Datacenter Type", order = 3, required = true, example = "Primary")
    private String datacenterType;

    @ExcelColumn(value = "Location Name", order = 4, required = true, example = "Andheri Office")
    private String locationName;

    // For export only - populated from relationships (NO @ExcelColumn)
    private String cityName;
    private String stateName;
}
