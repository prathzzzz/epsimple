package com.eps.module.api.epsone.payment_details.constant;

public class PaymentDetailsErrorMessages {
    public static final String PAYMENT_DETAILS_NOT_FOUND_ID = "Payment details not found with ID: ";
    public static final String PAYMENT_METHOD_NOT_FOUND_ID = "Payment method not found with ID: ";
    
    // Bulk Upload Validation Messages
    public static final String PAYMENT_METHOD_NAME_REQUIRED = "Payment method name is required";
    public static final String PAYMENT_METHOD_NOT_FOUND_NAME = "Payment method '%s' does not exist";
    public static final String TRANSACTION_NUMBER_TOO_LONG = "Transaction number cannot exceed 255 characters";
    public static final String VPA_TOO_LONG = "VPA cannot exceed 255 characters";
    public static final String BENEFICIARY_NAME_TOO_LONG = "Beneficiary name cannot exceed 255 characters";
    public static final String BENEFICIARY_ACCOUNT_NUMBER_TOO_LONG = "Beneficiary account number cannot exceed 255 characters";
    
    private PaymentDetailsErrorMessages() {}
}
