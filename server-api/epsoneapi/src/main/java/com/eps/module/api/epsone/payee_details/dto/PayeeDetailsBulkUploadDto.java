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
public class PayeeDetailsBulkUploadDto {

    @ExcelColumn(value = "Payee Name", order = 1, required = true, example = "ABC Corporation")
    private String payeeName;

    @ExcelColumn(value = "PAN Number", order = 2, required = false, example = "ABCDE1234F")
    private String panNumber;

    @ExcelColumn(value = "Aadhaar Number", order = 3, required = false, example = "123456789012")
    private String aadhaarNumber;

    @ExcelColumn(value = "Bank Name", order = 4, required = false, example = "State Bank of India")
    private String bankName;

    @ExcelColumn(value = "IFSC Code", order = 5, required = false, example = "SBIN0001234")
    private String ifscCode;

    @ExcelColumn(value = "Beneficiary Name", order = 6, required = false, example = "John Doe")
    private String beneficiaryName;

    @ExcelColumn(value = "Account Number", order = 7, required = false, example = "1234567890")
    private String accountNumber;
}
