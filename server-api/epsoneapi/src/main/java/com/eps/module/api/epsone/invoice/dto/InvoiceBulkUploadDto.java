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
public class InvoiceBulkUploadDto {

    // Basic Invoice Information
    @ExcelColumn(value = "Invoice Number", order = 1, required = true, example = "INV-2024-001")
    private String invoiceNumber;

    @ExcelColumn(value = "Invoice Date", order = 2, required = true, example = "2024-01-15")
    private String invoiceDate;

    @ExcelColumn(value = "Invoice Received Date", order = 3, example = "2024-01-17")
    private String invoiceReceivedDate;

    @ExcelColumn(value = "Order Number", order = 4, example = "ORD-2024-001")
    private String orderNumber;

    @ExcelColumn(value = "Vendor Name", order = 5, example = "ABC Suppliers Ltd")
    private String vendorName;

    // Foreign Keys (Lookup by Name/Code)
    @ExcelColumn(value = "Payee Name", order = 6, required = true, example = "ABC Suppliers Ltd")
    private String payeeName;

    @ExcelColumn(value = "Payment Transaction Number", order = 7, example = "TXN-2024-001")
    private String paymentTransactionNumber;

    // Payment Information
    @ExcelColumn(value = "Payment Due Date", order = 8, example = "2024-02-15")
    private String paymentDueDate;

    @ExcelColumn(value = "Payment Status", order = 9, example = "PENDING")
    private String paymentStatus;

    @ExcelColumn(value = "Paid Date", order = 10, example = "2024-02-10")
    private String paidDate;

    // Quantity & Pricing
    @ExcelColumn(value = "Quantity", order = 11, example = "10")
    private String quantity;

    @ExcelColumn(value = "Unit", order = 12, example = "PCS")
    private String unit;

    @ExcelColumn(value = "Unit Price", order = 13, example = "1000.00")
    private String unitPrice;

    // Tax Percentages
    @ExcelColumn(value = "Tax CGST Percentage", order = 14, example = "9.00")
    private String taxCgstPercentage;

    @ExcelColumn(value = "Tax SGST Percentage", order = 15, example = "9.00")
    private String taxSgstPercentage;

    @ExcelColumn(value = "Tax IGST Percentage", order = 16, example = "18.00")
    private String taxIgstPercentage;

    // Amount Breakdowns
    @ExcelColumn(value = "Basic Amount", order = 17, example = "10000.00")
    private String basicAmount;

    @ExcelColumn(value = "CGST", order = 18, example = "900.00")
    private String cgst;

    @ExcelColumn(value = "SGST", order = 19, example = "900.00")
    private String sgst;

    @ExcelColumn(value = "IGST", order = 20, example = "1800.00")
    private String igst;

    @ExcelColumn(value = "Amount 1", order = 21, example = "500.00")
    private String amount1;

    @ExcelColumn(value = "Amount 2", order = 22, example = "500.00")
    private String amount2;

    // Discounts & Deductions
    @ExcelColumn(value = "Discount Percentage", order = 23, example = "5.00")
    private String discountPercentage;

    @ExcelColumn(value = "Discount Amount", order = 24, example = "500.00")
    private String discountAmount;

    @ExcelColumn(value = "TDS", order = 25, example = "200.00")
    private String tds;

    @ExcelColumn(value = "Advance Amount", order = 26, example = "1000.00")
    private String advanceAmount;

    // Total Amounts
    @ExcelColumn(value = "Total Amount", order = 27, example = "12300.00")
    private String totalAmount;

    @ExcelColumn(value = "Total Invoice Value", order = 28, required = true, example = "12300.00")
    private String totalInvoiceValue;

    @ExcelColumn(value = "Net Payable", order = 29, example = "11100.00")
    private String netPayable;

    // Machine & PO Details
    @ExcelColumn(value = "Machine Serial Number", order = 30, example = "SN-ATM-12345")
    private String machineSerialNumber;

    @ExcelColumn(value = "Master PO Number", order = 31, example = "MPO-2024-001")
    private String masterPoNumber;

    @ExcelColumn(value = "Master PO Date", order = 32, example = "2024-01-01")
    private String masterPoDate;

    @ExcelColumn(value = "Dispatch Order Number", order = 33, example = "DO-2024-001")
    private String dispatchOrderNumber;

    @ExcelColumn(value = "Dispatch Order Date", order = 34, example = "2024-01-10")
    private String dispatchOrderDate;

    // Banking & GST Details
    @ExcelColumn(value = "UTR Detail", order = 35, example = "UTR123456789")
    private String utrDetail;

    @ExcelColumn(value = "Billed By Vendor GST", order = 36, example = "29ABCDE1234F1Z5")
    private String billedByVendorGst;

    @ExcelColumn(value = "Billed To EPS GST", order = 37, example = "29XYZAB5678G1H9")
    private String billedToEpsGst;

    // Remarks
    @ExcelColumn(value = "Remarks", order = 38, example = "Additional notes or comments")
    private String remarks;
}
