package com.eps.module.api.epsone.landlord.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandlordErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Error Message", order = 2)
    private String errorMessage;

    @ExcelColumn(value = "Contact Number", order = 3)
    private String contactNumber;

    @ExcelColumn(value = "Rent Share Percentage", order = 4)
    private BigDecimal rentSharePercentage;
}
