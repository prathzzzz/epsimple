package com.eps.module.common.exception;

import lombok.Getter;

@Getter
public class ForeignKeyConstraintException extends RuntimeException {
    private final String recordName;
    private final String resourceType;
    private final String dependentResource;
    
    public ForeignKeyConstraintException(String recordName, String resourceType, String dependentResource) {
        super(String.format("Cannot delete '%s' because it is being used by %s", recordName, dependentResource));
        this.recordName = recordName;
        this.resourceType = resourceType;
        this.dependentResource = dependentResource;
    }
    
    public ForeignKeyConstraintException(String recordName, String resourceType, String dependentResource, Throwable cause) {
        super(String.format("Cannot delete '%s' because it is being used by %s", recordName, dependentResource), cause);
        this.recordName = recordName;
        this.resourceType = resourceType;
        this.dependentResource = dependentResource;
    }
}
