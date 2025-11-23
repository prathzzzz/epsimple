package com.eps.module.api.epsone.landlord.constant;

public class LandlordErrorMessages {
    public static final String LANDLORD_NOT_FOUND_ID = "Landlord not found with id: ";
    public static final String PERSON_DETAILS_NOT_FOUND_ID = "Person details not found with id: ";
    public static final String PERSON_DETAILS_ALREADY_ASSOCIATED = "Person details with id %s is already associated with another landlord";
    public static final String CANNOT_DELETE_LANDLORD_PAYEES = "Cannot delete landlord as it has %d associated payees";
    
    // Bulk Upload Validation Messages
    public static final String CONTACT_NUMBER_REQUIRED = "Contact Number is required";
    public static final String CONTACT_NUMBER_INVALID_FORMAT = "Contact Number must be exactly 10 digits";
    public static final String PERSON_DETAILS_NOT_FOUND_CONTACT = "Person Details not found with Contact Number: %s. Please upload Person Details first.";
    public static final String PERSON_ALREADY_LANDLORD = "Person with Contact Number %s is already assigned as a landlord";
    public static final String RENT_SHARE_INVALID_RANGE = "Rent Share Percentage must be between 0 and 100";
    public static final String RENT_SHARE_INVALID_SCALE = "Rent Share Percentage can have maximum 2 decimal places";
    
    private LandlordErrorMessages() {}
}
