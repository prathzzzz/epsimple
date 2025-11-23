package com.eps.module.api.epsone.vendor_type.constant;

public class VendorTypeErrorMessages {
    public static final String VENDOR_TYPE_NOT_FOUND_ID = "Vendor type not found with id: ";
    public static final String VENDOR_TYPE_NAME_EXISTS = "Vendor type with name '%s' already exists";
    public static final String VENDOR_CATEGORY_NOT_FOUND_ID = "Vendor category not found with id: ";
    public static final String VENDOR_CATEGORY_NOT_FOUND_NAME = "Vendor category '%s' not found";
    public static final String CANNOT_DELETE_VENDOR_TYPE_USED = "Cannot delete '%s' vendor type because it is being used by %d vendor(s): %s";
    public static final String CANNOT_DELETE_VENDOR_TYPE_USED_MORE = " and %d more";
    public static final String CANNOT_DELETE_VENDOR_TYPE_SUFFIX = ". Please delete or reassign these vendors first.";
    
    // Validation messages
    public static final String VENDOR_TYPE_NAME_REQUIRED = "Vendor type name is required";
    public static final String VENDOR_TYPE_NAME_MAX_LENGTH = "Vendor type name cannot exceed 100 characters";
    public static final String VENDOR_CATEGORY_NAME_REQUIRED = "Vendor category name is required";
    public static final String DESCRIPTION_MAX_LENGTH = "Description cannot exceed 5000 characters";
    
    private VendorTypeErrorMessages() {}
}
