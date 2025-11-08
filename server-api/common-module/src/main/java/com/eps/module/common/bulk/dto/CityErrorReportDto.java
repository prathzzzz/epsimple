package com.eps.module.common.bulk.dto;

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
    
    // State fields
    @ExcelColumn(value = "State Name", order = 4)
    private String stateName;
    
    @ExcelColumn(value = "State Code", order = 5)
    private String stateCode;
    
    @ExcelColumn(value = "State Code Alt", order = 6)
    private String stateCodeAlt;
    
    // Error fields
    @ExcelColumn(value = "Error Type", order = 7, required = true)
    private String errorType;
    
    @ExcelColumn(value = "Error Message", order = 8, required = true)
    private String errorMessage;
}
