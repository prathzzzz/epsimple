package com.eps.module.api.epsone.invoice.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    // Basic Invoice Information
    @ExcelColumn(value = "Invoice Number", order = 2)
    private String invoiceNumber;

    @ExcelColumn(value = "Invoice Date", order = 3)
    private String invoiceDate;

    @ExcelColumn(value = "Invoice Received Date", order = 4)
    private String invoiceReceivedDate;

    @ExcelColumn(value = "Order Number", order = 5)
    private String orderNumber;

    @ExcelColumn(value = "Vendor Name", order = 6)
    private String vendorName;

    // Foreign Keys
    @ExcelColumn(value = "Payee Name", order = 7)
    private String payeeName;

    @ExcelColumn(value = "Payment Transaction Number", order = 8)
    private String paymentTransactionNumber;

    // Payment Information
    @ExcelColumn(value = "Payment Due Date", order = 9)
    private String paymentDueDate;

    @ExcelColumn(value = "Payment Status", order = 10)
    private String paymentStatus;

    @ExcelColumn(value = "Paid Date", order = 11)
    private String paidDate;

    // Quantity & Pricing
    @ExcelColumn(value = "Quantity", order = 12)
    private String quantity;

    @ExcelColumn(value = "Unit", order = 13)
    private String unit;

    @ExcelColumn(value = "Unit Price", order = 14)
    private String unitPrice;

    // Tax Percentages
    @ExcelColumn(value = "Tax CGST Percentage", order = 15)
    private String taxCgstPercentage;

    @ExcelColumn(value = "Tax SGST Percentage", order = 16)
    private String taxSgstPercentage;

    @ExcelColumn(value = "Tax IGST Percentage", order = 17)
    private String taxIgstPercentage;

    // Amount Breakdowns
    @ExcelColumn(value = "Basic Amount", order = 18)
    private String basicAmount;

    @ExcelColumn(value = "CGST", order = 19)
    private String cgst;

    @ExcelColumn(value = "SGST", order = 20)
    private String sgst;

    @ExcelColumn(value = "IGST", order = 21)
    private String igst;

    @ExcelColumn(value = "Amount 1", order = 22)
    private String amount1;

    @ExcelColumn(value = "Amount 2", order = 23)
    private String amount2;

    // Discounts & Deductions
    @ExcelColumn(value = "Discount Percentage", order = 24)
    private String discountPercentage;

    @ExcelColumn(value = "Discount Amount", order = 25)
    private String discountAmount;

    @ExcelColumn(value = "TDS", order = 26)
    private String tds;

    @ExcelColumn(value = "Advance Amount", order = 27)
    private String advanceAmount;

    // Total Amounts
    @ExcelColumn(value = "Total Amount", order = 28)
    private String totalAmount;

    @ExcelColumn(value = "Total Invoice Value", order = 29)
    private String totalInvoiceValue;

    @ExcelColumn(value = "Net Payable", order = 30)
    private String netPayable;

    // Machine & PO Details
    @ExcelColumn(value = "Machine Serial Number", order = 31)
    private String machineSerialNumber;

    @ExcelColumn(value = "Master PO Number", order = 32)
    private String masterPoNumber;

    @ExcelColumn(value = "Master PO Date", order = 33)
    private String masterPoDate;

    @ExcelColumn(value = "Dispatch Order Number", order = 34)
    private String dispatchOrderNumber;

    @ExcelColumn(value = "Dispatch Order Date", order = 35)
    private String dispatchOrderDate;

    // Banking & GST Details
    @ExcelColumn(value = "UTR Detail", order = 36)
    private String utrDetail;

    @ExcelColumn(value = "Billed By Vendor GST", order = 37)
    private String billedByVendorGst;

    @ExcelColumn(value = "Billed To EPS GST", order = 38)
    private String billedToEpsGst;

    // Remarks
    @ExcelColumn(value = "Remarks", order = 39)
    private String remarks;

    // Error Information
    @ExcelColumn(value = "Error", order = 40)
    private String error;
}
