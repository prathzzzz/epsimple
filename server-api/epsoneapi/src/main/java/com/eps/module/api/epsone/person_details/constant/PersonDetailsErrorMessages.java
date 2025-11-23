package com.eps.module.api.epsone.person_details.constant;

public class PersonDetailsErrorMessages {
    public static final String PERSON_DETAILS_NOT_FOUND_ID = "Person details not found with ID: ";
    public static final String PERSON_TYPE_NOT_FOUND_ID = "Person type not found with ID: ";
    public static final String CANNOT_DELETE_USED_BY_VENDOR = "Cannot delete person details for '%s' because it is being used by a vendor. Please delete the vendor first.";
    public static final String CANNOT_DELETE_USED_BY_LANDLORD = "Cannot delete person details for '%s' because it is being used by a landlord. Please delete the landlord first.";
    
    // Bulk Upload Validation Messages
    public static final String PERSON_TYPE_NAME_REQUIRED = "Person type name is required";
    public static final String PERSON_TYPE_NAME_TOO_LONG = "Person type name cannot exceed 150 characters";
    public static final String PERSON_TYPE_NOT_FOUND_NAME = "Person type not found: ";
    public static final String FIRST_NAME_TOO_LONG = "First name cannot exceed 100 characters";
    public static final String MIDDLE_NAME_TOO_LONG = "Middle name cannot exceed 100 characters";
    public static final String LAST_NAME_TOO_LONG = "Last name cannot exceed 100 characters";
    public static final String CONTACT_NUMBER_INVALID = "Contact number must be exactly 10 digits";
    public static final String PERMANENT_ADDRESS_TOO_LONG = "Permanent address cannot exceed 5000 characters";
    public static final String CORRESPONDENCE_ADDRESS_TOO_LONG = "Correspondence address cannot exceed 5000 characters";
    
    private PersonDetailsErrorMessages() {}
}
