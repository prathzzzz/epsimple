package com.eps.module.api.epsone.warehouse.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseErrorReportDto {

    @ExcelColumn(value = "Row Number")
    private Integer rowNumber;

    @ExcelColumn(value = "Error Type")
    private String errorType;

    @ExcelColumn(value = "Error Message")
    private String errorMessage;

    @ExcelColumn(value = "Warehouse Name")
    private String warehouseName;

    @ExcelColumn(value = "Warehouse Code")
    private String warehouseCode;

    @ExcelColumn(value = "Warehouse Type")
    private String warehouseType;

    @ExcelColumn(value = "Location Name")
    private String locationName;

    @ExcelColumn(value = "Location Address")
    private String locationAddress;

    @ExcelColumn(value = "City Name")
    private String cityName;

    @ExcelColumn(value = "State Name")
    private String stateName;
}
