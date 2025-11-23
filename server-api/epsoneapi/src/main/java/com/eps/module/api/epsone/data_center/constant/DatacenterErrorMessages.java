package com.eps.module.api.epsone.data_center.constant;

public class DatacenterErrorMessages {
    public static final String DATACENTER_NOT_FOUND_ID = "Datacenter not found with id: ";
    public static final String DATACENTER_NOT_FOUND_AFTER_SAVE = "Datacenter not found after save";
    public static final String DATACENTER_NOT_FOUND_AFTER_UPDATE = "Datacenter not found after update";
    public static final String LOCATION_NOT_FOUND_ID = "Location not found with id: ";
    public static final String DATACENTER_CODE_EXISTS = "Datacenter code '%s' already exists";
    public static final String CANNOT_DELETE_DATACENTER_ASSETS = "Cannot delete datacenter '%s' because it has %d asset%s. Please remove the assets from this datacenter first.";
    
    // Bulk Upload Validation Messages
    public static final String DATACENTER_NAME_REQUIRED = "Datacenter name is required";
    public static final String DATACENTER_NAME_LENGTH_EXCEEDED = "Datacenter name cannot exceed 255 characters";
    public static final String DATACENTER_CODE_LENGTH_EXCEEDED = "Datacenter code cannot exceed 50 characters";
    public static final String DATACENTER_CODE_INVALID_FORMAT = "Datacenter code must contain only uppercase letters, numbers, hyphens, and underscores";
    public static final String DATACENTER_TYPE_LENGTH_EXCEEDED = "Datacenter type cannot exceed 100 characters";
    public static final String LOCATION_NAME_REQUIRED = "Location name is required";
    public static final String LOCATION_NOT_FOUND_NAME = "Location not found: ";

    private DatacenterErrorMessages() {}
}
