package com.eps.module.api.epsone.voucher.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Voucher Number", order = 2)
    private String voucherNumber;

    @ExcelColumn(value = "Voucher Date", order = 3)
    private String voucherDate;

    @ExcelColumn(value = "Order Number", order = 4)
    private String orderNumber;

    @ExcelColumn(value = "Payee Name", order = 5)
    private String payeeName;

    @ExcelColumn(value = "Payment Transaction Number", order = 6)
    private String paymentTransactionNumber;

    @ExcelColumn(value = "Payment Due Date", order = 7)
    private String paymentDueDate;

    @ExcelColumn(value = "Payment Status", order = 8)
    private String paymentStatus;

    @ExcelColumn(value = "Quantity", order = 9)
    private String quantity;

    @ExcelColumn(value = "Unit", order = 10)
    private String unit;

    @ExcelColumn(value = "Unit Price", order = 11)
    private String unitPrice;

    @ExcelColumn(value = "Tax CGST", order = 12)
    private String taxCgst;

    @ExcelColumn(value = "Tax SGST", order = 13)
    private String taxSgst;

    @ExcelColumn(value = "Tax IGST", order = 14)
    private String taxIgst;

    @ExcelColumn(value = "Amount 1", order = 15)
    private String amount1;

    @ExcelColumn(value = "Amount 2", order = 16)
    private String amount2;

    @ExcelColumn(value = "Discount Percentage", order = 17)
    private String discountPercentage;

    @ExcelColumn(value = "Discount Amount", order = 18)
    private String discountAmount;

    @ExcelColumn(value = "Final Amount", order = 19)
    private String finalAmount;

    @ExcelColumn(value = "Remarks", order = 20)
    private String remarks;

    @ExcelColumn(value = "Error Type", order = 21)
    private String errorType;

    @ExcelColumn(value = "Error Message", order = 22)
    private String errorMessage;
}
