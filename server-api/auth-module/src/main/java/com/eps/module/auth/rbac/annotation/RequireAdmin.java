package com.eps.module.auth.rbac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to protect controller methods or entire classes - requires admin access (ALL permission).
 * 
 * Can be applied at method level or class level.
 * When applied at class level, all methods in the class require admin access.
 * 
 * Examples:
 * // Method level
 * @RequireAdmin
 * public ResponseEntity<RoleDTO> createRole(...) { }
 * 
 * // Class level (applies to all methods)
 * @RequireAdmin
 * public class RoleController { }
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAdmin {
}
