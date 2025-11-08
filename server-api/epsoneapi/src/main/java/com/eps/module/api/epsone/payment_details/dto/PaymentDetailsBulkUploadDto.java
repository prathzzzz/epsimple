package com.eps.module.api.epsone.payment_details.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsBulkUploadDto {

    @ExcelColumn(value = "Payment Method Name", order = 1, required = true, example = "Credit Card")
    private String paymentMethodName;

    @ExcelColumn(value = "Payment Date", order = 2, required = false, example = "2024-01-15")
    private LocalDate paymentDate;

    @ExcelColumn(value = "Payment Amount", order = 3, required = false, example = "5000.00")
    private BigDecimal paymentAmount;

    @ExcelColumn(value = "Transaction Number", order = 4, required = false, example = "TXN123456789")
    private String transactionNumber;

    @ExcelColumn(value = "VPA", order = 5, required = false, example = "user@upi")
    private String vpa;

    @ExcelColumn(value = "Beneficiary Name", order = 6, required = false, example = "John Doe")
    private String beneficiaryName;

    @ExcelColumn(value = "Beneficiary Account Number", order = 7, required = false, example = "1234567890")
    private String beneficiaryAccountNumber;

    @ExcelColumn(value = "Payment Remarks", order = 8, required = false, example = "Payment for services")
    private String paymentRemarks;
}
