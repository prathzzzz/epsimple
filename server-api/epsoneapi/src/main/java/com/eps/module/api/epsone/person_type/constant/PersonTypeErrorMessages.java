package com.eps.module.api.epsone.person_type.constant;

public class PersonTypeErrorMessages {
    public static final String PERSON_TYPE_NOT_FOUND_ID = "Person type not found with id: ";
    public static final String PERSON_TYPE_NAME_EXISTS = "Person type with name '%s' already exists";
    public static final String CANNOT_DELETE_PERSON_TYPE_USED = "Cannot delete '%s' person type because it is being used by %d person detail%s: %s";
    public static final String CANNOT_DELETE_PERSON_TYPE_USED_MORE = " and %d more";
    public static final String CANNOT_DELETE_PERSON_TYPE_SUFFIX = ". Please delete or reassign these person details first.";
    
    // Validation messages
    public static final String PERSON_TYPE_NAME_REQUIRED = "Person type name is required";
    public static final String PERSON_TYPE_NAME_MAX_LENGTH = "Person type name cannot exceed 100 characters";
    public static final String DESCRIPTION_MAX_LENGTH = "Description cannot exceed 5000 characters";
    
    private PersonTypeErrorMessages() {}
}
