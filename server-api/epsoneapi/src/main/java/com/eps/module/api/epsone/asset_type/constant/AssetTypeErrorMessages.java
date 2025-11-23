package com.eps.module.api.epsone.asset_type.constant;

public class AssetTypeErrorMessages {
    public static final String ASSET_TYPE_NOT_FOUND_ID = "Asset type not found with id: ";
    public static final String ASSET_TYPE_NAME_ALREADY_EXISTS = "Asset type with name '%s' already exists";
    public static final String ASSET_TYPE_CODE_ALREADY_EXISTS = "Asset type with code '%s' already exists";
    public static final String CANNOT_DELETE_ASSET_TYPE_IN_USE = "Cannot delete '%s' asset type because it is being used by %d asset%s. Please delete or reassign these assets first.";

    // Bulk Upload Validation Messages
    public static final String TYPE_NAME_REQUIRED = "Type name is required";
    public static final String TYPE_NAME_MAX_LENGTH = "Type name cannot exceed 100 characters";
    public static final String TYPE_CODE_REQUIRED = "Type code is required";
    public static final String TYPE_CODE_MAX_LENGTH = "Type code cannot exceed 20 characters";
    public static final String TYPE_CODE_INVALID_FORMAT = "Type code must be uppercase alphanumeric with hyphens/underscores";
    public static final String DESCRIPTION_MAX_LENGTH = "Description cannot exceed 5000 characters";

    private AssetTypeErrorMessages() {}
}
