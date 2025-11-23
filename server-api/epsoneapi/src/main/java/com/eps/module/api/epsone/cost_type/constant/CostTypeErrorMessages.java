package com.eps.module.api.epsone.cost_type.constant;

public class CostTypeErrorMessages {
    public static final String COST_TYPE_NOT_FOUND = "Cost type not found: ";
    public static final String COST_TYPE_NOT_FOUND_ID = "Cost type not found with id: ";
    public static final String COST_CATEGORY_NOT_FOUND_ID = "Cost category not found with id: ";
    public static final String CANNOT_DELETE_COST_TYPE_IN_USE = "Cannot delete cost type '%s' because it is being used by %d cost item(s). Please delete the cost item(s) first.";
    public static final String TYPE_NAME_REQUIRED = "Type name is required";
    public static final String TYPE_NAME_TOO_LONG = "Type name cannot exceed 100 characters";
    public static final String TYPE_DESCRIPTION_REQUIRED = "Type description is required";
    public static final String COST_CATEGORY_NAME_REQUIRED = "Cost category name is required";
    public static final String COST_CATEGORY_NOT_FOUND_NAME = "Cost category '%s' does not exist";

    private CostTypeErrorMessages() {}
}
