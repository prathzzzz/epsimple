package com.eps.module.api.epsone.payee_details.constant;

public class PayeeDetailsErrorMessages {
    public static final String PAYEE_DETAILS_NOT_FOUND_ID = "Payee details with ID %d not found";
    public static final String BANK_NOT_FOUND_ID = "Bank with ID %d not found";
    public static final String PAN_ALREADY_REGISTERED = "PAN number is already registered";
    public static final String AADHAAR_ALREADY_REGISTERED = "Aadhaar number is already registered";
    public static final String ACCOUNT_ALREADY_REGISTERED = "Account number is already registered with %s";
    public static final String PAN_ALREADY_REGISTERED_OTHER = "PAN number is already registered with another payee";
    public static final String AADHAAR_ALREADY_REGISTERED_OTHER = "Aadhaar number is already registered with another payee";
    public static final String ACCOUNT_ALREADY_REGISTERED_OTHER = "Account number is already registered with %s for another payee";
    public static final String CANNOT_DELETE_PAYEE_DETAILS_USED = "Cannot delete payee details as it has %d associated payees";
    
    // Bulk Upload Validation Messages
    public static final String PAYEE_NAME_REQUIRED = "Payee name is required";
    public static final String PAYEE_NAME_TOO_LONG = "Payee name cannot exceed 255 characters";
    public static final String BANK_NOT_FOUND_NAME = "Bank '%s' does not exist";
    public static final String PAN_ALREADY_REGISTERED_VALUE = "PAN number '%s' is already registered";
    public static final String AADHAAR_ALREADY_REGISTERED_VALUE = "Aadhaar number '%s' is already registered";
    public static final String ACCOUNT_ALREADY_REGISTERED_BANK = "Account number '%s' is already registered with %s";
    public static final String PAN_TOO_LONG = "PAN number cannot exceed 255 characters";
    public static final String AADHAAR_TOO_LONG = "Aadhaar number cannot exceed 255 characters";
    public static final String IFSC_TOO_LONG = "IFSC code cannot exceed 20 characters";
    public static final String BENEFICIARY_NAME_TOO_LONG = "Beneficiary name cannot exceed 255 characters";
    public static final String ACCOUNT_NUMBER_TOO_LONG = "Account number cannot exceed 255 characters";
    
    private PayeeDetailsErrorMessages() {}
}
