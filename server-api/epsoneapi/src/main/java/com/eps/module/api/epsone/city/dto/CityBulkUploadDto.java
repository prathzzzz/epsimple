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

    @ExcelColumn(value = "City Code", order = 2, required = false, example = "MUM")
    private String cityCode;

    @ExcelColumn(value = "State Code", order = 3, required = true, example = "MH")
    private String stateCode;
}
