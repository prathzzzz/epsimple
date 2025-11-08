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
public class StateBulkUploadDto {
    
    @ExcelColumn(value = "State Name", order = 1, required = true, example = "Maharashtra")
    private String stateName;
    
    @ExcelColumn(value = "State Code", order = 2, required = true, example = "MH")
    private String stateCode;
    
    @ExcelColumn(value = "Alternate State Code", order = 3, required = true, example = "MAH")
    private String stateCodeAlt;
}
