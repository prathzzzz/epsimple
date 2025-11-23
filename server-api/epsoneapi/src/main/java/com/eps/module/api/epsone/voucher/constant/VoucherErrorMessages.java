package com.eps.module.api.epsone.voucher.constant;

public class VoucherErrorMessages {
    public static final String VOUCHER_NOT_FOUND = "Voucher not found: ";
    public static final String VOUCHER_NOT_FOUND_ID = "Voucher not found with ID: ";
    public static final String VOUCHER_NUMBER_EXISTS = "Voucher number '%s' already exists";
    public static final String PAYEE_NOT_FOUND_ID = "Payee not found with ID: ";
    public static final String PAYMENT_DETAILS_NOT_FOUND_ID = "Payment details not found with ID: ";
    
    // Bulk Upload Messages
    public static final String VOUCHER_NUMBER_REQUIRED = "Voucher number is required";
    public static final String VOUCHER_DATE_REQUIRED = "Voucher date is required";
    public static final String INVALID_DATE_FORMAT = "Invalid date format. Use yyyy-MM-dd, dd/MM/yyyy, dd-MM-yyyy, or M/d/yyyy";
    public static final String PAYEE_NAME_REQUIRED = "Payee name is required";
    public static final String PAYEE_NOT_FOUND_NAME = "Payee not found: ";
    public static final String FINAL_AMOUNT_REQUIRED = "Final amount is required";
    public static final String INVALID_NUMBER_FORMAT = "Invalid number format";
    public static final String PAYMENT_DETAILS_NOT_FOUND_TXN = "Payment details not found: ";

    private VoucherErrorMessages() {}
}
