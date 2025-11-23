package com.eps.module.api.epsone.warehouse.constant;

public class WarehouseErrorMessages {
    public static final String WAREHOUSE_NOT_FOUND_ID = "Warehouse not found with id: ";
    public static final String WAREHOUSE_NOT_FOUND_AFTER_SAVE = "Warehouse not found after save";
    public static final String WAREHOUSE_NOT_FOUND_AFTER_UPDATE = "Warehouse not found after update";
    public static final String LOCATION_NOT_FOUND_ID = "Location not found with id: ";
    public static final String WAREHOUSE_CODE_EXISTS = "Warehouse code '%s' already exists";
    public static final String CANNOT_DELETE_WAREHOUSE_ASSETS = "Cannot delete warehouse '%s' because it has %d asset%s. Please remove the assets from this warehouse first.";
    
    // Bulk Upload Validation Messages
    public static final String WAREHOUSE_NAME_REQUIRED = "Warehouse Name is required";
    public static final String LOCATION_NAME_REQUIRED = "Location Name is required";
    public static final String WAREHOUSE_NAME_LENGTH_EXCEEDED = "Warehouse Name must not exceed 255 characters";
    public static final String WAREHOUSE_CODE_LENGTH_EXCEEDED = "Warehouse Code must not exceed 50 characters";
    public static final String WAREHOUSE_TYPE_LENGTH_EXCEEDED = "Warehouse Type must not exceed 100 characters";
    public static final String LOCATION_NAME_LENGTH_EXCEEDED = "Location Name must not exceed 100 characters";
    public static final String LOCATION_NOT_FOUND_NAME = "Location '%s' not found in system";
    public static final String WAREHOUSE_CODE_INVALID_FORMAT = "Warehouse Code must contain only uppercase letters, numbers, hyphens, and underscores";

    private WarehouseErrorMessages() {}
}
