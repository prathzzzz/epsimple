package com.eps.module.api.epsone.vendor_category.constant;

public class VendorCategoryErrorMessages {
    public static final String VENDOR_CATEGORY_NOT_FOUND = "Vendor category not found: ";
    public static final String VENDOR_CATEGORY_NOT_FOUND_ID = "Vendor category not found with id: ";
    public static final String VENDOR_CATEGORY_ALREADY_EXISTS = "Vendor category '%s' already exists";
    public static final String CANNOT_DELETE_VENDOR_CATEGORY_IN_USE = "Cannot delete '%s' vendor category because it is being used by %d vendor type%s. Please delete or reassign these vendor types first.";
    
    // Validation messages
    public static final String CATEGORY_NAME_REQUIRED = "Category name is required";
    public static final String CATEGORY_NAME_TOO_LONG = "Category name cannot exceed 100 characters";
    
    private VendorCategoryErrorMessages() {}
}
