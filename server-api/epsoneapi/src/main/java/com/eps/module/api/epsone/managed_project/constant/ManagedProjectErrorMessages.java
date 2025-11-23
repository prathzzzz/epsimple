package com.eps.module.api.epsone.managed_project.constant;

public class ManagedProjectErrorMessages {
    public static final String MANAGED_PROJECT_NOT_FOUND = "Managed Project not found with id: ";
    public static final String BANK_NOT_FOUND = "Bank not found with id: ";
    public static final String PROJECT_CODE_ALREADY_EXISTS = "Project with code '%s' already exists";
    public static final String CANNOT_DELETE_USED_BY_SITES = "Cannot delete '%s' Managed Project because it is being used by %d site%s: %s. Please delete or reassign these sites first.";
    
    // Bulk Upload Validation Messages
    public static final String PROJECT_NAME_REQUIRED = "Project name is required";
    public static final String PROJECT_NAME_TOO_LONG = "Project name cannot exceed 255 characters";
    public static final String PROJECT_CODE_TOO_LONG = "Project code cannot exceed 50 characters";
    public static final String PROJECT_CODE_INVALID_FORMAT = "Project code can only contain letters, numbers, hyphens and underscores";
    public static final String PROJECT_TYPE_TOO_LONG = "Project type cannot exceed 50 characters";
    public static final String PROJECT_DESCRIPTION_TOO_LONG = "Project description cannot exceed 5000 characters";
    public static final String BANK_NAME_REQUIRED = "Bank name is required";
    public static final String DUPLICATE_PROJECT_NAME = "Project name already exists";
    public static final String DUPLICATE_PROJECT_CODE = "Project code '%s' already exists";
    
    private ManagedProjectErrorMessages() {}
}
