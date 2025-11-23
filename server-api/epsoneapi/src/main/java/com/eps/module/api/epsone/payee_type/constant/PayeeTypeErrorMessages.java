package com.eps.module.api.epsone.payee_type.constant;

public class PayeeTypeErrorMessages {
    public static final String PAYEE_TYPE_NOT_FOUND_ID = "Payee type not found with ID: ";
    public static final String PAYEE_TYPE_EXISTS = "Payee type '%s' already exists";
    public static final String CANNOT_DELETE_PAYEE_TYPE_USED = "Cannot delete payee type as it has %d associated payees";
    public static final String PAYEE_TYPE_ALREADY_EXISTS = "Payee type already exists: ";
    
    // Validation messages
    public static final String PAYEE_TYPE_REQUIRED = "Payee type is required";
    public static final String PAYEE_TYPE_MAX_LENGTH = "Payee type cannot exceed 50 characters";
    public static final String PAYEE_CATEGORY_MAX_LENGTH = "Payee category cannot exceed 100 characters";
    
    private PayeeTypeErrorMessages() {}
}
