package com.eps.module.api.epsone.city.constant;

public class CityErrorMessages {
    public static final String CITY_NOT_FOUND_ID = "City not found with ID: ";
    public static final String STATE_NOT_FOUND_ID = "State not found with ID: ";
    public static final String CITY_CODE_EXISTS = "City code already exists: ";
    public static final String CANNOT_DELETE_CITY_LOCATIONS = "Cannot delete '%s' city because it is being used by %d location%s: %s%s. Please delete or reassign these locations first.";
    
    // Bulk Upload Validation Messages
    public static final String CITY_NAME_REQUIRED = "City name is required";
    public static final String CITY_NAME_LENGTH_EXCEEDED = "City name cannot exceed 100 characters";
    public static final String CITY_CODE_LENGTH_EXCEEDED = "City code cannot exceed 10 characters";
    public static final String CITY_CODE_INVALID_FORMAT = "City code can only contain letters, numbers, hyphens and underscores";
    public static final String STATE_NAME_REQUIRED = "State name is required";
    public static final String STATE_CODE_REQUIRED = "State code is required (or provide State Name to look it up)";
    public static final String STATE_CODE_ALT_REQUIRED = "Alternate state code is required (or provide State Name to look it up)";
    public static final String STATE_NOT_FOUND_NAME = "State not found with name: ";
    public static final String STATE_CODE_MISMATCH = "State code '%s' does not match state '%s' (expected: %s)";
    public static final String STATE_CODE_ALT_MISMATCH = "Alternate state code '%s' does not match state '%s' (expected: %s)";
    public static final String STATE_NO_ALT_CODE = "State '%s' does not have an alternate code";

    private CityErrorMessages() {}
}
