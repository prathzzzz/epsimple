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
public class PaymentDetailsErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1, required = true)
    private Integer rowNumber;

    @ExcelColumn(value = "Error Type", order = 2, required = true)
    private String errorType;

    @ExcelColumn(value = "Error Message", order = 3, required = true)
    private String errorMessage;

    @ExcelColumn(value = "Payment Method Name", order = 4)
    private String paymentMethodName;

    @ExcelColumn(value = "Payment Date", order = 5)
    private LocalDate paymentDate;

    @ExcelColumn(value = "Payment Amount", order = 6)
    private BigDecimal paymentAmount;

    @ExcelColumn(value = "Transaction Number", order = 7)
    private String transactionNumber;

    @ExcelColumn(value = "VPA", order = 8)
    private String vpa;

    @ExcelColumn(value = "Beneficiary Name", order = 9)
    private String beneficiaryName;

    @ExcelColumn(value = "Beneficiary Account Number", order = 10)
    private String beneficiaryAccountNumber;

    @ExcelColumn(value = "Payment Remarks", order = 11)
    private String paymentRemarks;
}
