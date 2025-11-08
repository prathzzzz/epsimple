package com.eps.module.api.epsone.location.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1, required = true)
    private Integer rowNumber;

    @ExcelColumn(value = "Location Name", order = 2)
    private String locationName;

    @ExcelColumn(value = "Address", order = 3)
    private String address;

    @ExcelColumn(value = "District", order = 4)
    private String district;

    @ExcelColumn(value = "City Name", order = 5)
    private String cityName;

    @ExcelColumn(value = "City Code", order = 6)
    private String cityCode;

    @ExcelColumn(value = "State Name", order = 7)
    private String stateName;

    @ExcelColumn(value = "State Code", order = 8)
    private String stateCode;

    @ExcelColumn(value = "Pincode", order = 9)
    private String pincode;

    @ExcelColumn(value = "Region", order = 10)
    private String region;

    @ExcelColumn(value = "Zone", order = 11)
    private String zone;

    @ExcelColumn(value = "Longitude", order = 12)
    private String longitude;

    @ExcelColumn(value = "Latitude", order = 13)
    private String latitude;

    @ExcelColumn(value = "Error Type", order = 14, required = true)
    private String errorType;

    @ExcelColumn(value = "Error Message", order = 15, required = true)
    private String errorMessage;
}
