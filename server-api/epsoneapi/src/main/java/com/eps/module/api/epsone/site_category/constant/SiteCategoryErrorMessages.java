package com.eps.module.api.epsone.site_category.constant;

public class SiteCategoryErrorMessages {
    public static final String SITE_CATEGORY_NOT_FOUND_ID = "Site Category not found with id: ";
    public static final String SITE_CATEGORY_NAME_EXISTS = "Site category with name '%s' already exists";
    public static final String SITE_CATEGORY_CODE_EXISTS = "Site category with code '%s' already exists";
    
    // Validation messages
    public static final String SITE_CATEGORY_NAME_REQUIRED = "Category name is required";
    public static final String SITE_CATEGORY_NAME_MAX_LENGTH = "Category name cannot exceed 100 characters";
    public static final String SITE_CATEGORY_CODE_MAX_LENGTH = "Category code cannot exceed 20 characters";
    public static final String SITE_CATEGORY_CODE_FORMAT = "Category Code must contain only uppercase letters, numbers, hyphens, and underscores";
    public static final String DESCRIPTION_MAX_LENGTH = "Description cannot exceed 5000 characters";
    
    private SiteCategoryErrorMessages() {}
}
