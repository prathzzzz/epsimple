package com.eps.module.api.epsone.activity.constant;

public class ActivityErrorMessages {
    public static final String ACTIVITY_NOT_FOUND_ID = "Activity not found with ID: ";
    public static final String MASTER_ACTIVITY_NOT_FOUND = "Master Activity not found with name: ";
    public static final String ACTIVITY_NAME_EXISTS = "Activity with name '%s' already exists";
    public static final String CANNOT_DELETE_ACTIVITY_USED = "Activity '%s' cannot be deleted because it is being used by the following Activities: %s. Please remove these dependencies first.";
    
    // Bulk Upload Messages
    public static final String ACTIVITY_NAME_REQUIRED = "Activity name is required";
    public static final String ACTIVITY_NAME_MAX_LENGTH = "Activity name cannot exceed 100 characters";
    public static final String ACTIVITY_DESCRIPTION_MAX_LENGTH = "Activity description cannot exceed 65535 characters";
    
    private ActivityErrorMessages() {}
}
