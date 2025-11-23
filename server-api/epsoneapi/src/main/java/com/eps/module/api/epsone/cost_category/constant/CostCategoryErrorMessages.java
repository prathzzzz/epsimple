package com.eps.module.api.epsone.cost_category.constant;

public class CostCategoryErrorMessages {
    public static final String COST_CATEGORY_NOT_FOUND = "Cost category not found: ";
    public static final String COST_CATEGORY_NOT_FOUND_ID = "Cost category not found with id: ";
    public static final String COST_CATEGORY_ALREADY_EXISTS = "Cost category '%s' already exists";
    public static final String CANNOT_DELETE_COST_CATEGORY_IN_USE = "Cannot delete '%s' cost category because it is being used by %d cost type%s: %s. Please delete or reassign these cost types first.";
    
    // Validation messages
    public static final String COST_CATEGORY_NAME_REQUIRED = "Category name is required";
    public static final String COST_CATEGORY_NAME_MAX_LENGTH = "Category name cannot exceed 50 characters";
    public static final String COST_CATEGORY_DESCRIPTION_REQUIRED = "Category description is required";
    
    private CostCategoryErrorMessages() {}
}
