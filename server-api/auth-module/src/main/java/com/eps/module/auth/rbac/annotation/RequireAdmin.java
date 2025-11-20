package com.eps.module.auth.rbac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to protect controller methods - requires admin access (ALL permission).
 * 
 * Example:
 * @RequireAdmin
 * public ResponseEntity<RoleDTO> createRole(...) { }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAdmin {
}
