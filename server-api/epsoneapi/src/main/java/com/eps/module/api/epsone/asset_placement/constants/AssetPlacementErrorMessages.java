package com.eps.module.api.epsone.asset_placement.constants;

public final class AssetPlacementErrorMessages {
    
    private AssetPlacementErrorMessages() {
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

    // Bulk Upload Messages
    public static final String ASSET_TAG_ID_REQUIRED = "Asset Tag ID is required";
    public static final String ASSET_TAG_NOT_FOUND = "Asset with tag ID '%s' not found";
    public static final String LOCATION_CODE_REQUIRED = "Location Code is required";
    public static final String LOCATION_NOT_FOUND = "Location with code '%s' not found in Sites, Datacenters, or Warehouses";
    public static final String PLACEMENT_STATUS_CODE_REQUIRED = "Placement Status Code is required";
    public static final String PLACEMENT_STATUS_NOT_FOUND = "Placement Status with code '%s' not found";
}