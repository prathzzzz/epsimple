package com.eps.module.api.epsone.payee.constant;

public class PayeeErrorMessages {
    public static final String PAYEE_DETAILS_NOT_FOUND = "Payee details not found: ";
    public static final String PAYEE_NOT_FOUND_FOR_DETAILS = "Payee not found for payee details: ";
    public static final String PAYEE_TYPE_NOT_FOUND = "Payee type not found with id: ";
    public static final String PAYEE_DETAILS_ALREADY_ASSIGNED = "Payee details with id %d is already assigned to another payee";
    public static final String VENDOR_NOT_FOUND = "Vendor not found with id: ";
    public static final String LANDLORD_NOT_FOUND = "Landlord not found with id: ";
    public static final String PAYEE_NOT_FOUND = "Payee not found with id: ";
    public static final String CANNOT_DELETE_PAYEE_INVOICE_REF = "Cannot delete payee. It is referenced in %d invoice(s).";
    public static final String CANNOT_DELETE_PAYEE_VOUCHER_REF = "Cannot delete payee. It is referenced in %d voucher(s).";
    
    // Bulk Upload Validation Messages
    public static final String PAYEE_TYPE_REQUIRED = "Payee type is required";
    public static final String PAYEE_TYPE_NOT_FOUND_NAME = "Payee type '%s' does not exist";
    public static final String PAYEE_NAME_REQUIRED = "Payee name is required";
    public static final String PAYEE_DETAILS_NOT_FOUND_NAME = "Payee details with name '%s' does not exist";
    public static final String PAYEE_DETAILS_ALREADY_ASSIGNED_NAME = "Payee details '%s' is already assigned to another payee";
    public static final String VENDOR_NOT_FOUND_CONTACT = "Vendor with contact number '%s' does not exist";
    public static final String LANDLORD_NOT_FOUND_CONTACT = "Landlord with contact number '%s' does not exist";
    
    private PayeeErrorMessages() {}
}
