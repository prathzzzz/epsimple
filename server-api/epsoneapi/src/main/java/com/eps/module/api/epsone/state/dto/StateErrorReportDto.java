package com.eps.module.api.epsone.state.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateErrorReportDto {
    
    @ExcelColumn(value = "Row Number", order = 1, required = true)
    private Integer rowNumber;
    
    // State fields
    @ExcelColumn(value = "State Name", order = 2)
    private String stateName;
    
    @ExcelColumn(value = "State Code", order = 3)
    private String stateCode;
    
    @ExcelColumn(value = "Alternate State Code", order = 4)
    private String stateCodeAlt;
    
    // Error fields
    @ExcelColumn(value = "Error Type", order = 5, required = true)
    private String errorType;
    
    @ExcelColumn(value = "Error Message", order = 6, required = true)
    private String errorMessage;
}
