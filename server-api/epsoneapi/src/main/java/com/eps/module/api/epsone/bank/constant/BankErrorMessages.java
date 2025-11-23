package com.eps.module.api.epsone.bank.constant;

public class BankErrorMessages {
    public static final String BANK_NOT_FOUND = "Bank not found: ";
    public static final String BANK_NOT_FOUND_ID = "Bank not found with ID: ";
    public static final String CANNOT_DELETE_BANK_MANAGED_PROJECT = "Cannot delete '%s' bank because it is being used by %d Managed Project%s: %s%s. Please delete or reassign these Managed Projects first.";
    public static final String CANNOT_DELETE_BANK_PAYEE_DETAILS = "Cannot delete '%s' bank because it is being used by %d payee detail%s: %s%s. Please delete or reassign these payee details first.";
    
    // Bulk Upload Validation Messages
    public static final String BANK_NAME_REQUIRED = "Bank name is required";
    public static final String BANK_NAME_TOO_LONG = "Bank name cannot exceed 255 characters";
    public static final String RBI_CODE_TOO_LONG = "RBI bank code cannot exceed 10 characters";
    public static final String RBI_CODE_INVALID_FORMAT = "RBI bank code can only contain letters and numbers";
    public static final String RBI_CODE_EXISTS = "RBI bank code '%s' already exists";
    public static final String EPS_CODE_TOO_LONG = "EPS bank code cannot exceed 10 characters";
    public static final String EPS_CODE_INVALID_FORMAT = "EPS bank code can only contain letters, numbers, hyphens and underscores";
    public static final String EPS_CODE_EXISTS = "EPS bank code '%s' already exists";
    public static final String ALT_CODE_TOO_LONG = "Alternate bank code cannot exceed 10 characters";
    public static final String ALT_CODE_INVALID_FORMAT = "Alternate bank code can only contain letters, numbers, hyphens and underscores";
    public static final String ALT_CODE_EXISTS = "Alternate bank code '%s' already exists";
    public static final String LOGO_URL_TOO_LONG = "Bank logo URL cannot exceed 500 characters";
    
    private BankErrorMessages() {}
}
