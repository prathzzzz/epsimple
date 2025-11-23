package com.eps.module.api.epsone.asset.constant;

public class AssetErrorMessages {
    public static final String ASSET_NOT_FOUND_TAG = "Asset not found with tag: ";
    public static final String ASSET_NOT_FOUND_ID = "Asset not found with id: ";
    public static final String ASSET_CATEGORY_NOT_FOUND = "Asset Category not found: ";
    public static final String ASSET_TYPE_NOT_FOUND = "Asset Type not found: ";
    public static final String GENERATED_TAG_INVALID_LENGTH = "Generated asset tag is too short or too long";
    public static final String GENERATED_TAG_INVALID_CHARS = "Generated asset tag contains invalid characters";
    public static final String AUTO_GENERATE_TAG_ERROR = "Cannot auto-generate asset tag: ";
    
    public static final String ASSET_TAG_ALREADY_EXISTS = "Asset tag already exists: ";
    public static final String SERIAL_NUMBER_ALREADY_EXISTS = "Serial number already exists: ";
    public static final String ASSET_NOT_FOUND_AFTER_SAVE = "Asset not found after save";
    public static final String ASSET_NOT_FOUND_AFTER_UPDATE = "Asset not found after update";
    public static final String CANNOT_DELETE_ASSET_PLACED_ON_SITE = "Cannot delete asset '%s' because it is placed on %d site%s. Please remove the asset from these sites first.";
    public static final String CANNOT_DELETE_ASSET_PLACED_IN_WAREHOUSE = "Cannot delete asset '%s' because it is placed in %d warehouse%s. Please remove the asset from these warehouses first.";
    public static final String CANNOT_DELETE_ASSET_PLACED_IN_DATACENTER = "Cannot delete asset '%s' because it is placed in %d datacenter%s. Please remove the asset from these datacenters first.";
    
    // Bulk Upload Validation Messages
    public static final String ASSET_TAG_LENGTH_INVALID = "Asset Tag ID must be between 5 and 50 characters";
    public static final String ASSET_TAG_FORMAT_INVALID = "Asset Tag ID must be uppercase alphanumeric";
    public static final String ASSET_TYPE_REQUIRED = "Asset Type is required";
    public static final String ASSET_CATEGORY_REQUIRED = "Asset Category is required";
    public static final String VENDOR_CODE_REQUIRED = "Vendor Code is required";
    public static final String VENDOR_NOT_FOUND_CODE = "Vendor with code '%s' does not exist";
    public static final String LENDER_BANK_REQUIRED = "Lender Bank Name is required";
    public static final String LENDER_BANK_NOT_FOUND = "Bank '%s' does not exist";
    public static final String STATUS_CODE_NOT_FOUND = "Status Code '%s' does not exist";
    public static final String WARRANTY_PERIOD_NEGATIVE = "Warranty Period cannot be negative";
    public static final String WARRANTY_PERIOD_INVALID = "Warranty Period must be a valid integer";
    public static final String LOCATION_CODE_NOT_FOUND = "Location Code '%s' not found in Site, Datacenter, or Warehouse";
    public static final String PLACEMENT_STATUS_REQUIRED = "Placement Status Code is required when Location Code is specified";
    public static final String PLACEMENT_STATUS_NOT_FOUND = "Placement Status Code '%s' does not exist";
    public static final String DATE_FORMAT_INVALID = "%s must be in YYYY-MM-DD format or a valid Excel date/serial";
    public static final String DECIMAL_NEGATIVE = "%s cannot be negative";
    public static final String DECIMAL_INVALID = "%s must be a valid decimal number";
    public static final String LENGTH_EXCEEDED = "%s cannot exceed %d characters";

    private AssetErrorMessages() {}
}
