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
public class WarehouseBulkUploadDto {

    @ExcelColumn(value = "Warehouse Name", order = 1, required = true, example = "Main Warehouse")
    private String warehouseName;

    @ExcelColumn(value = "Warehouse Code", order = 2, required = false, example = "WH001")
    private String warehouseCode;

    @ExcelColumn(value = "Warehouse Type", order = 3, required = false, example = "Distribution Center")
    private String warehouseType;

    @ExcelColumn(value = "Location Name", order = 4, required = true, example = "Andheri Office")
    private String locationName;

    // For export only - populated from relationships
    private String locationAddress;
    private String cityName;
    private String stateName;
}
