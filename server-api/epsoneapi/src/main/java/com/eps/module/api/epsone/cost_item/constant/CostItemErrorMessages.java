package com.eps.module.api.epsone.cost_item.constant;

public class CostItemErrorMessages {
    public static final String COST_ITEM_NOT_FOUND = "Cost item not found: ";
    public static final String COST_ITEM_NOT_FOUND_ID = "Cost item not found with ID: ";
    public static final String COST_TYPE_NOT_FOUND_ID = "Cost type not found with ID: ";
    public static final String COST_ITEM_ALREADY_EXISTS_FOR_TYPE = "Cost item already exists for cost type: ";
    
    public static final String COST_ITEM_FOR_REQUIRED = "Cost item for is required";
    public static final String COST_ITEM_FOR_TOO_LONG = "Cost item for cannot exceed 255 characters";
    public static final String COST_TYPE_NAME_REQUIRED = "Cost type name is required";
    public static final String COST_TYPE_NOT_FOUND_NAME = "Cost type '%s' does not exist";

    private CostItemErrorMessages() {}
}
