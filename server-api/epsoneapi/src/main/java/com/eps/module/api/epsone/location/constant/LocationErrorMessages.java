package com.eps.module.api.epsone.location.constant;

public class LocationErrorMessages {
    public static final String LOCATION_NOT_FOUND_ID = "Location not found with ID: ";
    public static final String CITY_NOT_FOUND_ID = "City not found with ID: ";
    public static final String CANNOT_DELETE_LOCATION_IN_USE_BY_SITES = "Cannot delete '%s' location because it is being used by %d site%s: %s%s. Please delete or reassign these sites first.";
    public static final String CANNOT_DELETE_LOCATION_IN_USE_BY_WAREHOUSES = "Cannot delete '%s' location because it is being used by %d warehouse%s: %s%s. Please delete or reassign these warehouses first.";
    public static final String CANNOT_DELETE_LOCATION_IN_USE_BY_DATACENTERS = "Cannot delete '%s' location because it is being used by %d datacenter%s: %s%s. Please delete or reassign these datacenters first.";

    // Bulk Upload Validation Messages
    public static final String LOCATION_NAME_REQUIRED = "Location name is required";
    public static final String LOCATION_NAME_TOO_LONG = "Location name cannot exceed 255 characters";
    public static final String ADDRESS_TOO_LONG = "Address cannot exceed 5000 characters";
    public static final String DISTRICT_TOO_LONG = "District name cannot exceed 100 characters";
    public static final String CITY_NAME_REQUIRED = "City name is required";
    public static final String CITY_NOT_FOUND_NAME = "City not found: ";
    public static final String PINCODE_INVALID_FORMAT = "Pincode must be exactly 6 digits";
    public static final String REGION_TOO_LONG = "Region cannot exceed 50 characters";
    public static final String ZONE_TOO_LONG = "Zone cannot exceed 50 characters";
    public static final String LONGITUDE_INVALID_RANGE = "Longitude must be between -180 and 180";
    public static final String LONGITUDE_INVALID_FORMAT = "Invalid longitude format";
    public static final String LATITUDE_INVALID_RANGE = "Latitude must be between -90 and 90";
    public static final String LATITUDE_INVALID_FORMAT = "Invalid latitude format";

    private LocationErrorMessages() {}
}
