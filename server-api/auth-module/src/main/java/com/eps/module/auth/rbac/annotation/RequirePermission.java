package com.eps.module.auth.rbac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to protect controller methods with permission checking.
 * User must have the specified permission to access the method.
 * 
 * Example:
 * @RequirePermission("ASSET:CREATE")
 * public ResponseEntity<AssetDTO> createAsset(...) { }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    /**
     * The permission required to access this method
     */
    String value();
}
