package com.eps.module.api.epsone.invoice.constant;

public class InvoiceErrorMessages {
    public static final String INVOICE_NOT_FOUND = "Invoice not found with number: ";
    public static final String INVOICE_NOT_FOUND_GENERIC = "Invoice not found: ";
    public static final String EXPENDITURE_INVOICE_NOT_FOUND = "Expenditure Invoice not found for invoice number: ";
    
    public static final String INVOICE_NOT_FOUND_ID = "Invoice not found with ID: ";
    public static final String INVOICE_NUMBER_EXISTS = "Invoice number '%s' already exists";
    public static final String PAYEE_NOT_FOUND_ID = "Payee not found with ID: ";
    public static final String PAYMENT_DETAILS_NOT_FOUND_ID = "Payment details not found with ID: ";
    
    // Bulk Upload Messages
    public static final String INVOICE_NUMBER_REQUIRED = "Invoice number is required";
    public static final String INVOICE_NUMBER_TOO_LONG = "Invoice number cannot exceed 100 characters";
    public static final String INVOICE_DATE_REQUIRED = "Invoice date is required";
    public static final String INVALID_DATE_FORMAT = "Invalid date format. Use yyyy-MM-dd, dd/MM/yyyy, or MM/dd/yyyy";
    public static final String ORDER_NUMBER_TOO_LONG = "Order number cannot exceed 100 characters";
    public static final String VENDOR_NAME_TOO_LONG = "Vendor name cannot exceed 255 characters";
    public static final String PAYEE_NAME_REQUIRED = "Payee name is required";
    public static final String PAYEE_NOT_FOUND_NAME = "Payee '%s' not found";
    public static final String PAYMENT_TXN_NOT_FOUND = "Payment transaction '%s' not found";
    public static final String PAYMENT_STATUS_TOO_LONG = "Payment status cannot exceed 20 characters";
    public static final String INVALID_NUMBER_FORMAT = "Invalid number format";
    public static final String TOTAL_INVOICE_VALUE_REQUIRED = "Total invoice value is required";
    public static final String FIELD_TOO_LONG = "%s cannot exceed %d characters";

    private InvoiceErrorMessages() {}
}
