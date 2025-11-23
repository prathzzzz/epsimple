package com.eps.module.api.epsone.generic_status_type.constant;

public class GenericStatusTypeErrorMessages {
    public static final String STATUS_TYPE_NOT_FOUND = "Status type not found: ";
    public static final String STATUS_TYPE_NOT_FOUND_ID = "Generic status type not found with ID: ";
    public static final String STATUS_NAME_EXISTS = "Status type with name '%s' already exists";
    public static final String STATUS_CODE_EXISTS = "Status type with code '%s' already exists";
    public static final String CANNOT_DELETE_STATUS_ACTIVITY_WORK = "Cannot delete status type as it has %d associated activity work orders";
    
    // Bulk Upload Validation Messages
    public static final String STATUS_NAME_REQUIRED = "Status Name is required";
    public static final String STATUS_NAME_LENGTH_EXCEEDED = "Status Name cannot exceed 100 characters";
    public static final String STATUS_CODE_LENGTH_EXCEEDED = "Status Code cannot exceed 20 characters";
    public static final String STATUS_CODE_INVALID_FORMAT = "Status Code must contain only uppercase letters and spaces";
    public static final String DESCRIPTION_LENGTH_EXCEEDED = "Description cannot exceed 5000 characters";

    private GenericStatusTypeErrorMessages() {}
}
