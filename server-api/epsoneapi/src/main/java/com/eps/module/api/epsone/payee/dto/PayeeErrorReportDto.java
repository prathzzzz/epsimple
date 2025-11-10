package com.eps.module.api.epsone.payee.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayeeErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1, required = true)
    private Integer rowNumber;

    @ExcelColumn(value = "Error Type", order = 2, required = true)
    private String errorType;

    @ExcelColumn(value = "Error Message", order = 3, required = true)
    private String errorMessage;

    @ExcelColumn(value = "Payee Type", order = 4)
    private String payeeType;

    @ExcelColumn(value = "Payee Name", order = 5)
    private String payeeName;

    @ExcelColumn(value = "Vendor Email", order = 6)
    private String vendorEmail;

    @ExcelColumn(value = "Landlord Email", order = 7)
    private String landlordEmail;
}
