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
public class LocationBulkUploadDto {

    @ExcelColumn(value = "Location Name", order = 1, required = true, example = "Andheri Office")
    private String locationName;

    @ExcelColumn(value = "Address", order = 2, required = true, example = "123 Main Street, Andheri West")
    private String address;

    @ExcelColumn(value = "District", order = 3, required = true, example = "Mumbai Suburban")
    private String district;

    @ExcelColumn(value = "City Name", order = 4, required = true, example = "Mumbai")
    private String cityName;

    @ExcelColumn(value = "Pincode", order = 5, required = true, example = "400058")
    private String pincode;

    @ExcelColumn(value = "Region", order = 6, required = true, example = "West")
    private String region;

    @ExcelColumn(value = "Zone", order = 7, required = true, example = "Zone A")
    private String zone;

    @ExcelColumn(value = "Longitude", order = 8, required = true, example = "72.8777")
    private String longitude;

    @ExcelColumn(value = "Latitude", order = 9, required = true, example = "19.0760")
    private String latitude;

    // For export - populated from relationship (not in upload template)
    private String cityCode;
    private String stateName;
    private String stateCode;
}
