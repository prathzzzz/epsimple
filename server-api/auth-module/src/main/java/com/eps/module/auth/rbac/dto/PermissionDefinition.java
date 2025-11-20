package com.eps.module.auth.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for permission definition used by the registry
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDefinition {
    private String name;          // e.g., "ASSET:CREATE"
    private String description;    // e.g., "Can create assets"
    private String scope;          // e.g., "ASSET"
    private String action;         // e.g., "CREATE"
    private String category;       // e.g., "Operations"
}
