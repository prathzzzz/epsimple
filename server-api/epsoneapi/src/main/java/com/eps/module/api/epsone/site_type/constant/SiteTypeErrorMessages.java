package com.eps.module.api.epsone.site_type.constant;

public class SiteTypeErrorMessages {
    public static final String SITE_TYPE_NOT_FOUND_ID = "Site Type not found with id: ";
    public static final String SITE_TYPE_NAME_EXISTS = "Site type with name '%s' already exists";
    
    // Validation messages
    public static final String SITE_TYPE_NAME_REQUIRED = "Type name is required";
    public static final String SITE_TYPE_NAME_MAX_LENGTH = "Type name cannot exceed 100 characters";
    public static final String DESCRIPTION_MAX_LENGTH = "Description cannot exceed 5000 characters";
    
    private SiteTypeErrorMessages() {}
}
