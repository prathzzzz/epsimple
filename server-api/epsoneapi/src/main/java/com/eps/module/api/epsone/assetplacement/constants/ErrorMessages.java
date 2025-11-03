package com.eps.module.api.epsone.assetplacement.constants;

/**
 * Constant error messages for asset placement module
 */
public final class ErrorMessages {
    
    private ErrorMessages() {
        throw new IllegalStateException("Utility class");
    }
    
    public static final String ASSET_NOT_FOUND = "Asset not found with id: ";
    public static final String ASSET_PLACEMENT_NOT_FOUND = "Asset placement not found with id: ";
    public static final String ASSET_PLACEMENT_NOT_FOUND_AFTER_SAVE = "Asset placement not found after save";
    public static final String ASSET_PLACEMENT_NOT_FOUND_AFTER_UPDATE = "Asset placement not found after update";
    
    public static final String SITE_NOT_FOUND = "Site not found with id: ";
    public static final String WAREHOUSE_NOT_FOUND = "Warehouse not found with id: ";
    public static final String DATACENTER_NOT_FOUND = "Datacenter not found with id: ";
    
    public static final String ASSET_STATUS_NOT_FOUND = "Asset status not found with id: ";
    public static final String ACTIVITY_WORK_NOT_FOUND = "Activity work not found with id: ";
}
