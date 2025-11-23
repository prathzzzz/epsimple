package com.eps.module.api.epsone.activities.constant;

public class ActivitiesErrorMessages {
    public static final String ACTIVITIES_NOT_FOUND_ID = "Activities not found with ID: ";
    public static final String ACTIVITIES_NOT_FOUND_NAME = "Activities not found with name: ";
    public static final String ACTIVITIES_NAME_EXISTS = "Activities with name '%s' already exists";
    public static final String ACTIVITY_NOT_FOUND_ID = "Activity not found with ID: ";
    public static final String CANNOT_DELETE_ACTIVITIES_USED = "Cannot delete activities as it has %d associated activity work orders";
    
    // Bulk Upload Messages
    public static final String MASTER_ACTIVITY_NAME_REQUIRED = "Master activity name is required";
    public static final String MASTER_ACTIVITY_NOT_FOUND_NAME = "Master activity '%s' not found";
    public static final String ACTIVITY_NAME_REQUIRED = "Activity name is required";
    public static final String ACTIVITY_NAME_MAX_LENGTH = "Activity name cannot exceed 100 characters";
    public static final String ACTIVITY_CATEGORY_MAX_LENGTH = "Activity category cannot exceed 100 characters";
    public static final String ACTIVITY_DESCRIPTION_MAX_LENGTH = "Activity description cannot exceed 5000 characters";
    
    private ActivitiesErrorMessages() {}
}
