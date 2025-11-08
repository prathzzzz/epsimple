package com.eps.module.api.epsone.payee_details.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayeeDetailsErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1, required = true)
    private Integer rowNumber;

    @ExcelColumn(value = "Error Type", order = 2, required = true)
    private String errorType;

    @ExcelColumn(value = "Error Message", order = 3, required = true)
    private String errorMessage;

    @ExcelColumn(value = "Payee Name", order = 4)
    private String payeeName;

    @ExcelColumn(value = "PAN Number", order = 5)
    private String panNumber;

    @ExcelColumn(value = "Aadhaar Number", order = 6)
    private String aadhaarNumber;

    @ExcelColumn(value = "Bank Name", order = 7)
    private String bankName;

    @ExcelColumn(value = "IFSC Code", order = 8)
    private String ifscCode;

    @ExcelColumn(value = "Beneficiary Name", order = 9)
    private String beneficiaryName;

    @ExcelColumn(value = "Account Number", order = 10)
    private String accountNumber;
}
