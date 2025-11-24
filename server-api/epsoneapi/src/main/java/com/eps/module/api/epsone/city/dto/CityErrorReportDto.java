package com.eps.module.api.epsone.city.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityErrorReportDto {
    
    @ExcelColumn(value = "Row Number", order = 1, required = true)
    private Integer rowNumber;
    
    // City fields
    @ExcelColumn(value = "City Name", order = 2)
    private String cityName;
    
    @ExcelColumn(value = "City Code", order = 3)
    private String cityCode;
    
    // State field
    @ExcelColumn(value = "State Code", order = 4)
    private String stateCode;
    
    // Error fields
    @ExcelColumn(value = "Error Type", order = 5, required = true)
    private String errorType;
    
    @ExcelColumn(value = "Error Message", order = 6, required = true)
    private String errorMessage;
}
