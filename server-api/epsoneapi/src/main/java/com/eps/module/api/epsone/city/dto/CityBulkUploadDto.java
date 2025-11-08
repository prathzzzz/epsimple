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
public class CityBulkUploadDto {

    @ExcelColumn(value = "City Name", order = 1, required = true, example = "Mumbai")
    private String cityName;

    @ExcelColumn(value = "City Code", order = 2, required = true, example = "MUM")
    private String cityCode;

    @ExcelColumn(value = "State Name", order = 3, required = true, example = "Maharashtra")
    private String stateName;

    @ExcelColumn(value = "State Code", order = 4, required = true, example = "MH")
    private String stateCode;

    @ExcelColumn(value = "State Code Alt", order = 5, required = true, example = "MAH")
    private String stateCodeAlt;
}
