package com.eps.module.api.epsone.bank.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankErrorReportDto {

    @ExcelColumn(value = "Row Number")
    private Integer rowNumber;

    @ExcelColumn(value = "Error Type")
    private String errorType;

    @ExcelColumn(value = "Error Message")
    private String errorMessage;

    @ExcelColumn(value = "Bank Name")
    private String bankName;

    @ExcelColumn(value = "RBI Bank Code")
    private String rbiBankCode;

    @ExcelColumn(value = "EPS Bank Code")
    private String epsBankCode;

    @ExcelColumn(value = "Alternate Bank Code")
    private String bankCodeAlt;

    // Bank logo is uploaded separately, not part of error report
    private String bankLogo;
}
