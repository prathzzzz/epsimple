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
public class DatacenterErrorReportDto {

    @ExcelColumn(value = "Row Number")
    private Integer rowNumber;

    @ExcelColumn(value = "Error Type")
    private String errorType;

    @ExcelColumn(value = "Error Message")
    private String errorMessage;

    @ExcelColumn(value = "Datacenter Name")
    private String datacenterName;

    @ExcelColumn(value = "Datacenter Code")
    private String datacenterCode;

    @ExcelColumn(value = "Datacenter Type")
    private String datacenterType;

    @ExcelColumn(value = "Location Name")
    private String locationName;

    @ExcelColumn(value = "City Name")
    private String cityName;

    @ExcelColumn(value = "State Name")
    private String stateName;
}
