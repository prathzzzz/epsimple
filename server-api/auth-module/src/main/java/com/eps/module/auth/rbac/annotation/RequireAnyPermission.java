package com.eps.module.auth.rbac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to protect controller methods - requires user to have at least one of the specified permissions.
 * 
 * Example:
 * @RequireAnyPermission({"ASSET:CREATE", "ASSET:UPDATE"})
 * public ResponseEntity<AssetDTO> saveAsset(...) { }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAnyPermission {
    /**
     * Array of permissions - user must have at least one
     */
    String[] value();
}
