package com.eps.module.api.epsone.expenditures_invoice.constant;

public class ExpendituresInvoiceErrorMessages {
    public static final String EXPENDITURES_INVOICE_NOT_FOUND = "Expenditures invoice not found with ID: ";
    public static final String COST_ITEM_NOT_FOUND = "Cost item not found with ID: ";
    public static final String INVOICE_NOT_FOUND = "Invoice not found with ID: ";
    public static final String MANAGED_PROJECT_NOT_FOUND = "Managed Project not found with ID: ";
    
    // Bulk Upload Validation Messages
    public static final String COST_ITEM_NAME_REQUIRED = "Cost Item Name is required";
    public static final String INVOICE_NUMBER_REQUIRED = "Invoice Number is required";
    public static final String MANAGED_PROJECT_CODE_REQUIRED = "Managed Project Code is required";
    public static final String INCURRED_DATE_REQUIRED = "Incurred Date is required";
    public static final String INCURRED_DATE_INVALID = "Incurred Date must be in format yyyy-MM-dd";
    public static final String COST_ITEM_NOT_FOUND_NAME = "Cost item not found with name: ";
    public static final String INVOICE_NOT_FOUND_NUMBER = "Invoice not found with number: ";
    public static final String MANAGED_PROJECT_NOT_FOUND_CODE = "Managed Project not found with code: ";

    private ExpendituresInvoiceErrorMessages() {}
}
