package com.eps.module.api.epsone.movement_type.constant;

public class MovementTypeErrorMessages {
    public static final String MOVEMENT_TYPE_NOT_FOUND_ID = "Movement type not found with id: ";
    public static final String MOVEMENT_TYPE_EXISTS = "Movement type '%s' already exists";
    
    // Bulk Upload Validation Messages
    public static final String MOVEMENT_TYPE_REQUIRED = "Movement type is required";
    public static final String MOVEMENT_TYPE_LENGTH_EXCEEDED = "Movement type must not exceed 100 characters";
    public static final String DESCRIPTION_LENGTH_EXCEEDED = "Description must not exceed 5000 characters";

    private MovementTypeErrorMessages() {}
}
