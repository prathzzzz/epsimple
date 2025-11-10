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
public class PayeeBulkUploadDto {

    @ExcelColumn(value = "Payee Type", order = 1, required = true, example = "Vendor")
    private String payeeType;

    @ExcelColumn(value = "Payee Name", order = 2, required = true, example = "ABC Corporation")
    private String payeeName;

    @ExcelColumn(value = "Vendor Email", order = 3, required = false, example = "vendor@example.com")
    private String vendorEmail;

    @ExcelColumn(value = "Landlord Email", order = 4, required = false, example = "landlord@example.com")
    private String landlordEmail;
}
