package com.eps.module.api.epsone.asset_category.constant;

public class AssetCategoryErrorMessages {
    public static final String ASSET_CATEGORY_NOT_FOUND_ID = "Asset category not found with id: ";
    public static final String ASSET_CATEGORY_NAME_ALREADY_EXISTS = "Asset category with name '%s' already exists";
    public static final String ASSET_CATEGORY_CODE_ALREADY_EXISTS = "Asset category with code '%s' already exists";
    public static final String ASSET_CODE_ALT_ALREADY_EXISTS = "Asset code alt '%s' already exists";
    public static final String CANNOT_DELETE_ASSET_CATEGORY_IN_USE = "Cannot delete asset category. It is referenced in %d asset(s).";

    // Validation messages
    public static final String ASSET_CATEGORY_NAME_REQUIRED = "Category name is required";
    public static final String ASSET_CATEGORY_NAME_MAX_LENGTH = "Category name must not exceed 100 characters";
    public static final String ASSET_CATEGORY_CODE_REQUIRED = "Category code is required";
    public static final String ASSET_CATEGORY_CODE_MAX_LENGTH = "Category code must not exceed 20 characters";
    public static final String ASSET_CATEGORY_CODE_FORMAT = "Category code must contain only uppercase letters, numbers, ampersand, hyphens, and underscores";
    public static final String ASSET_CODE_ALT_REQUIRED = "Asset code alt is required";
    public static final String ASSET_CODE_ALT_MAX_LENGTH = "Asset code alt must not exceed 10 characters";
    public static final String DESCRIPTION_MAX_LENGTH = "Description must not exceed 5000 characters";

    private AssetCategoryErrorMessages() {}
}
