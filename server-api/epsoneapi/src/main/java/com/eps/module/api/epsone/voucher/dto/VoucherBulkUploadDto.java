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
public class VoucherBulkUploadDto {

    @ExcelColumn(value = "Voucher Number", order = 1, required = true, example = "VCH-2024-001")
    private String voucherNumber;

    @ExcelColumn(value = "Voucher Date", order = 2, required = true, example = "2024-01-15")
    private String voucherDate;

    @ExcelColumn(value = "Order Number", order = 3, example = "ORD-2024-001")
    private String orderNumber;

    @ExcelColumn(value = "Payee Name", order = 4, required = true, example = "ABC Corporation")
    private String payeeName;

    @ExcelColumn(value = "Payment Transaction Number", order = 5, example = "TXN-2024-001")
    private String paymentTransactionNumber;

    @ExcelColumn(value = "Payment Due Date", order = 6, example = "2024-02-15")
    private String paymentDueDate;

    @ExcelColumn(value = "Payment Status", order = 7, example = "PENDING")
    private String paymentStatus;

    @ExcelColumn(value = "Quantity", order = 8, example = "10.50")
    private String quantity;

    @ExcelColumn(value = "Unit", order = 9, example = "PCS")
    private String unit;

    @ExcelColumn(value = "Unit Price", order = 10, example = "1500.00")
    private String unitPrice;

    @ExcelColumn(value = "Tax CGST", order = 11, example = "1350.00")
    private String taxCgst;

    @ExcelColumn(value = "Tax SGST", order = 12, example = "1350.00")
    private String taxSgst;

    @ExcelColumn(value = "Tax IGST", order = 13, example = "0.00")
    private String taxIgst;

    @ExcelColumn(value = "Amount 1", order = 14, example = "15000.00")
    private String amount1;

    @ExcelColumn(value = "Amount 2", order = 15, example = "0.00")
    private String amount2;

    @ExcelColumn(value = "Discount Percentage", order = 16, example = "5.00")
    private String discountPercentage;

    @ExcelColumn(value = "Discount Amount", order = 17, example = "750.00")
    private String discountAmount;

    @ExcelColumn(value = "Final Amount", order = 18, required = true, example = "17850.00")
    private String finalAmount;

    @ExcelColumn(value = "Remarks", order = 19, example = "Payment for services rendered")
    private String remarks;
}
