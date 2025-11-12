package com.eps.module.api.epsone.payment_method.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodBulkUploadDto {

    @ExcelColumn(value = "Method Name", order = 1, required = true, example = "NEFT")
    private String methodName;

    @ExcelColumn(value = "Description", order = 2, required = false, example = "Payment done through National Electronic Funds Transfer (NEFT)")
    private String description;
}
