package com.eps.module.api.epsone.asset_movement.constants;

/**
 * Constants for asset location types
 */
public final class LocationType {
    
    private LocationType() {
        throw new IllegalStateException("Utility class");
    }
    
    public static final String SITE = "site";
    public static final String WAREHOUSE = "warehouse";
    public static final String DATACENTER = "datacenter";
    public static final String FACTORY = "factory";
    public static final String NEWLY_PLACED = "newly-placed";
    
    // Capitalized versions for display
    public static final String SITE_DISPLAY = "Site";
    public static final String WAREHOUSE_DISPLAY = "Warehouse";
    public static final String DATACENTER_DISPLAY = "Datacenter";
    public static final String FACTORY_DISPLAY = "Factory";
    public static final String NEWLY_PLACED_DISPLAY = "Newly Placed";
}
