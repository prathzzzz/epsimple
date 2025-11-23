package com.eps.module.api.epsone.activity_work.constant;

public class ActivityWorkErrorMessages {
    public static final String ACTIVITY_WORK_NOT_FOUND_ID = "Activity Work not found with ID: ";
    public static final String ACTIVITIES_NOT_FOUND_ID = "Activity not found with id: ";
    public static final String VENDOR_NOT_FOUND_ID = "Vendor not found with id: ";
    public static final String STATUS_TYPE_NOT_FOUND_ID = "Status type not found with id: ";
    public static final String ACTIVITY_WORK_NOT_FOUND_AFTER_SAVE = "Activity work not found after save";
    public static final String ACTIVITY_WORK_NOT_FOUND_AFTER_UPDATE = "Activity work not found after update";
    public static final String CANNOT_DELETE_ACTIVITY_WORK_REMARKS = "Cannot delete activity work. It has %d remark(s) associated with it.";
    
    // Bulk Upload Messages
    public static final String ACTIVITIES_NAME_REQUIRED = "Activities name is required";
    public static final String ACTIVITIES_NOT_FOUND_NAME = "Activities '%s' not found";
    public static final String VENDOR_NAME_REQUIRED = "Vendor name is required";
    public static final String VENDOR_NOT_FOUND_NAME = "Vendor '%s' not found";
    public static final String VENDOR_ORDER_NUMBER_MAX_LENGTH = "Vendor order number cannot exceed 100 characters";
    public static final String INVALID_DATE_FORMAT = "Invalid date format. Use yyyy-MM-dd, dd/MM/yyyy, or MM/dd/yyyy";
    public static final String STATUS_TYPE_CODE_REQUIRED = "Status type code is required";
    public static final String STATUS_TYPE_CODE_NOT_FOUND = "Status type code '%s' not found";
    
    private ActivityWorkErrorMessages() {}
}
