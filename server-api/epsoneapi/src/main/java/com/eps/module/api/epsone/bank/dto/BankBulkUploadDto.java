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
public class BankBulkUploadDto {

    @ExcelColumn(value = "Bank Name", order = 1, required = true, example = "State Bank of India")
    private String bankName;

    @ExcelColumn(value = "RBI Bank Code", order = 2, required = true, example = "SBIN0001234")
    private String rbiBankCode;

    @ExcelColumn(value = "EPS Bank Code", order = 3, required = true, example = "SBI-01")
    private String epsBankCode;

    @ExcelColumn(value = "Alternate Bank Code", order = 4, required = true, example = "SBI_ALT")
    private String bankCodeAlt;

    // Bank logo is uploaded separately via multipart, not part of bulk upload
    private String bankLogo;
}
