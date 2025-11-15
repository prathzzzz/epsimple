package com.eps.module.common.constants;

/**
 * Centralized error message constants for the application.
 * This class provides standardized error messages to ensure consistency across all modules.
 */
public final class ErrorMessages {
    
    private ErrorMessages() {
        throw new IllegalStateException("Utility class");
    }
    
    // Generic error messages
    public static final String ENTITY_NOT_FOUND = "%s not found with ID: %s";
    public static final String ENTITY_NOT_FOUND_SIMPLE = "%s not found with id: %s";
    public static final String ENTITY_ALREADY_EXISTS = "%s '%s' already exists";
    public static final String ENTITY_CODE_ALREADY_EXISTS = "%s with code '%s' already exists";
    
    // Deletion constraint messages
    public static final String CANNOT_DELETE_HAS_ASSOCIATIONS = "Cannot delete %s as it has %d associated %s";
    public static final String CANNOT_DELETE_IN_USE = "Cannot delete '%s' %s because it is being used by %d %s: %s. Please delete or reassign these %s first.";
    public static final String CANNOT_DELETE_HAS_ASSETS = "Cannot delete %s '%s' because it has %d asset%s. Please remove the assets from this %s first.";
    public static final String CANNOT_DELETE_HAS_ITEMS = "Cannot delete %s '%s' because it is being used by %d %s. Please delete the %s first.";
    
    // Bank related messages
    public static final String BANK_NOT_FOUND = "Bank with ID %s not found";
    public static final String BANK_NOT_FOUND_WITH_ID = "Bank not found with id: %s";
    
    // State related messages
    public static final String STATE_NOT_FOUND = "State not found with ID: %s";
    public static final String STATE_NOT_FOUND_WITH_NAME = "State not found with name: %s";
    
    // City related messages
    public static final String CITY_NOT_FOUND = "City not found with ID: %s";
    public static final String CITY_CODE_ALREADY_EXISTS = "City code already exists: %s";
    
    // Asset related messages
    public static final String ASSET_NOT_FOUND = "Asset not found with id: %s";
    public static final String ASSET_CATEGORY_NOT_FOUND = "Asset category not found with id: %s";
    public static final String ASSET_STATUS_NOT_FOUND = "Asset status not found with id: %s";
    public static final String ASSET_TYPE_NOT_FOUND = "Asset type not found with id: %s";
    public static final String ASSET_PLACEMENT_NOT_FOUND = "Asset placement not found with id: %s";
    public static final String ASSET_PLACEMENT_NOT_FOUND_AFTER_SAVE = "Asset placement not found after save";
    public static final String ASSET_PLACEMENT_NOT_FOUND_AFTER_UPDATE = "Asset placement not found after update";
    
    // Vendor related messages
    public static final String VENDOR_NOT_FOUND = "Vendor not found with id: %s";
    
    // Location related messages
    public static final String SITE_NOT_FOUND = "Site not found with id: %s";
    public static final String WAREHOUSE_NOT_FOUND = "Warehouse not found with id: %s";
    public static final String DATACENTER_NOT_FOUND = "Datacenter not found with id: %s";
    public static final String FACTORY_NOT_FOUND = "Factory not found with id: %s";
    
    // Code generator messages
    public static final String CODE_GENERATOR_NOT_FOUND = "%s code generator not found with id: %s";
    public static final String CODE_GENERATOR_ALREADY_EXISTS = "%s code generator already exists for this %s combination";
    
    // Activity related messages
    public static final String ACTIVITY_WORK_NOT_FOUND = "Activity work not found with id: %s";
    public static final String ACTIVITY_WORK_HAS_REMARKS = "Cannot delete activity work. It has %d remark(s) associated with it.";
    
    // Payee related messages
    public static final String PAYEE_TYPE_NOT_FOUND = "Payee type not found with ID: %s";
    public static final String PAYEE_DETAILS_NOT_FOUND = "Payee details with ID %s not found";
    
    // Payment related messages
    public static final String PAYMENT_METHOD_NOT_FOUND = "Payment method not found with ID: %s";
    
    // Person related messages
    public static final String PERSON_TYPE_NOT_FOUND = "Person type not found with id: %s";
    
    // Project related messages
    public static final String MANAGED_PROJECT_NOT_FOUND = "Managed project not found with id: %s";
}
