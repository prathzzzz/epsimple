package com.eps.module.api.epsone.landlord.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandlordBulkUploadDto {

    @ExcelColumn(value = "Contact Number", order = 1, example = "9876543210")
    private String contactNumber;

    @ExcelColumn(value = "Rent Share Percentage", order = 2, example = "50.00")
    private BigDecimal rentSharePercentage;
}
