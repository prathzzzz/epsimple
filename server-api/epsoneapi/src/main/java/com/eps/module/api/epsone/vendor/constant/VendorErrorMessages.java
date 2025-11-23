package com.eps.module.api.epsone.vendor.constant;

public class VendorErrorMessages {
    public static final String VENDOR_NOT_FOUND_CODE = "Vendor not found with code: ";
    public static final String VENDOR_NOT_FOUND = "Vendor not found: ";
    public static final String VENDOR_TYPE_NOT_FOUND = "Vendor type not found with id: ";
    public static final String PERSON_DETAILS_NOT_FOUND = "Person details not found with id: ";
    public static final String PERSON_DETAILS_ALREADY_ASSOCIATED = "Person details with id %s is already associated with another vendor";
    public static final String VENDOR_CODE_EXISTS = "Vendor code '%s' already exists";
    public static final String VENDOR_NOT_FOUND_ID = "Vendor not found with id: ";
    public static final String CANNOT_DELETE_VENDOR_ACTIVITY_WORK = "Cannot delete vendor as it has %d associated activity work orders";
    public static final String CANNOT_DELETE_VENDOR_PAYEES = "Cannot delete vendor as it has %d associated payees";
    
    // Bulk Upload Validation Messages
    public static final String CONTACT_NUMBER_REQUIRED = "Contact Number is required";
    public static final String CONTACT_NUMBER_INVALID_FORMAT = "Contact Number must be exactly 10 digits";
    public static final String PERSON_DETAILS_NOT_FOUND_CONTACT = "Person Details not found with contact number: %s";
    public static final String PERSON_ALREADY_VENDOR = "Person with contact number %s is already a vendor";
    public static final String MULTIPLE_PERSON_RECORDS = "Multiple person records found with contact number %s. Please contact administrator to resolve data duplication.";
    public static final String VENDOR_TYPE_NAME_REQUIRED = "Vendor Type Name is required";
    public static final String VENDOR_TYPE_NAME_TOO_LONG = "Vendor Type Name cannot exceed 150 characters";
    public static final String VENDOR_TYPE_NOT_FOUND_NAME = "Vendor Type not found: %s";
    public static final String VENDOR_CODE_REQUIRED = "Vendor Code is required";
    public static final String VENDOR_CODE_TOO_LONG = "Vendor Code cannot exceed 10 characters";
    public static final String VENDOR_CODE_INVALID_FORMAT = "Vendor Code must contain only uppercase letters, numbers, hyphens, and underscores";
    
    private VendorErrorMessages() {}
}
