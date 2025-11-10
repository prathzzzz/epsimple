package com.eps.module.api.epsone.movement_type.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovementTypeBulkUploadDto {

    @ExcelColumn(value = "Movement Type", order = 1, example = "Datacenter to Site")
    private String movementType;

    @ExcelColumn(value = "Description", order = 2, example = "Asset movement from datacenter to site location")
    private String description;
}
