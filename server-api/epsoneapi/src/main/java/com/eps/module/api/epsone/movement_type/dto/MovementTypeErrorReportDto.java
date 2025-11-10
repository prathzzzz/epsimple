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
public class MovementTypeErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Movement Type", order = 2)
    private String movementType;

    @ExcelColumn(value = "Description", order = 3)
    private String description;

    @ExcelColumn(value = "Error Message", order = 4)
    private String errorMessage;
}
