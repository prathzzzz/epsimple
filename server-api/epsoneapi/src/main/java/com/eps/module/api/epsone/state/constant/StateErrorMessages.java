package com.eps.module.api.epsone.state.constant;

public class StateErrorMessages {
    public static final String STATE_NOT_FOUND_ID = "State not found with ID: ";
    public static final String STATE_NAME_EXISTS = "State with name '%s' already exists";
    public static final String STATE_CODE_EXISTS = "State with code '%s' already exists";
    public static final String CANNOT_DELETE_STATE_CITIES = "Cannot delete '%s' state because it is being used by %d %s: %s%s. Please delete or reassign these cities first.";
    
    // Bulk Upload Validation Messages
    public static final String STATE_NAME_REQUIRED = "State name is required";
    public static final String STATE_NAME_LENGTH_EXCEEDED = "State name cannot exceed 100 characters";
    public static final String STATE_CODE_REQUIRED = "State code is required";
    public static final String STATE_CODE_LENGTH_INVALID = "State code must be between 2 and 10 characters";
    public static final String STATE_CODE_INVALID_FORMAT = "State code can only contain letters, numbers, hyphens and underscores";
    public static final String STATE_CODE_ALT_LENGTH_EXCEEDED = "Alternate state code cannot exceed 10 characters";
    public static final String STATE_CODE_ALT_INVALID_FORMAT = "Alternate state code can only contain letters, numbers, hyphens and underscores";

    private StateErrorMessages() {}
}
