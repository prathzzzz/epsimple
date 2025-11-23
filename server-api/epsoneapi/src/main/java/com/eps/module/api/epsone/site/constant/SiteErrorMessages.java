package com.eps.module.api.epsone.site.constant;

public class SiteErrorMessages {
    public static final String SITE_NOT_FOUND = "Site not found with code: ";
    public static final String SITE_NOT_FOUND_ID = "Site not found with ID: ";
    public static final String GENERATED_SITE_CODE_INVALID_LENGTH = "Generated site code is too short or too long";
    public static final String GENERATED_SITE_CODE_INVALID_CHARS = "Generated site code contains invalid characters";
    public static final String AUTO_GENERATE_SITE_CODE_ERROR = "Cannot auto-generate site code: ";
    public static final String AUTO_GENERATE_SITE_CODE_MISSING_DATA = "Cannot auto-generate site code - missing required project or location data";
    public static final String DUPLICATE_SITE_CODE = "Duplicate site code: ";
    public static final String DUPLICATE_SITE_CODE_SUFFIX = " - This row should have been skipped by validator";
    public static final String SITE_CODE_EXISTS = "Site with code '%s' already exists";
    public static final String LOCATION_NOT_FOUND_ID = "Location not found with id: ";
    public static final String MANAGED_PROJECT_NOT_FOUND_ID = "Managed Project not found with id: ";
    public static final String SITE_CATEGORY_NOT_FOUND_ID = "Site Category not found with id: ";
    public static final String SITE_TYPE_NOT_FOUND_ID = "Site Type not found with id: ";
    public static final String SITE_STATUS_NOT_FOUND_ID = "Site Status not found with id: ";
    public static final String PERSON_DETAILS_NOT_FOUND_ID = "Person Details not found with id: ";
    public static final String CANNOT_DELETE_SITE_ASSETS = "Cannot delete site '%s' because it has %d asset%s assigned to it. Please remove or reassign these assets first.";
    public static final String CANNOT_DELETE_SITE_EXPENDITURE = "Cannot delete site '%s' because it has %d activity work expenditure record%s. Please remove these records first.";

    // Bulk Upload Validation Messages
    public static final String PROJECT_CODE_REQUIRED = "Project Code is required";
    public static final String PROJECT_NOT_FOUND_CODE = "Project with code '%s' does not exist";
    public static final String LOCATION_NAME_REQUIRED = "Location Name is required";
    public static final String LOCATION_NOT_FOUND_NAME = "Location with name '%s' does not exist";
    public static final String SITE_CODE_INVALID_FORMAT = "Site Code must be 5-50 uppercase alphanumeric characters";
    public static final String SITE_CATEGORY_NOT_FOUND_NAME = "Site Category '%s' does not exist";
    public static final String SITE_TYPE_NOT_FOUND_NAME = "Site Type '%s' does not exist";
    public static final String SITE_STATUS_NOT_FOUND_CODE = "Site Status '%s' does not exist";
    public static final String DATE_FORMAT_INVALID = "%s must be in YYYY-MM-DD format or a valid Excel date/serial (e.g. 44927) or common formats like dd/MM/yyyy";
    public static final String IP_ADDRESS_INVALID = "%s must be a valid IPv4 address";
    public static final String DECIMAL_NEGATIVE = "%s cannot be negative";
    public static final String DECIMAL_INVALID = "%s must be a valid decimal number";
    public static final String LENGTH_EXCEEDED = "%s cannot exceed %d characters";
    public static final String PHONE_NUMBER_INVALID = "%s must be a 10-digit phone number";
    public static final String PERSON_CONTACT_NOT_FOUND = "%s '%s' not found in person details. Please ensure the person is registered first.";

    private SiteErrorMessages() {}
}
