package com.eps.module.auth.rbac.aspect;

import com.eps.module.auth.rbac.annotation.RequireAdmin;
import com.eps.module.auth.rbac.annotation.RequireAnyPermission;
import com.eps.module.auth.rbac.annotation.RequirePermission;
import com.eps.module.auth.rbac.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * AOP Aspect for permission checking.
 * Intercepts methods annotated with @RequirePermission, @RequireAnyPermission, or @RequireAdmin
 * and validates user has required permissions before allowing method execution.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionAspect {

    private final PermissionService permissionService;

    /**
     * Intercept @RequirePermission annotations
     */
    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        String permission = requirePermission.value();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthenticated access attempt to method requiring permission: {}", permission);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        if (!permissionService.hasPermission(authentication, permission)) {
            log.warn("User {} attempted to access method requiring permission: {}", 
                    authentication.getName(), permission);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
                    "Access denied. Required permission: " + permission);
        }

        return joinPoint.proceed();
    }

    /**
     * Intercept @RequireAnyPermission annotations
     */
    @Around("@annotation(requireAnyPermission)")
    public Object checkAnyPermission(ProceedingJoinPoint joinPoint, RequireAnyPermission requireAnyPermission) throws Throwable {
        String[] permissions = requireAnyPermission.value();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthenticated access attempt to method requiring any of permissions: {}", (Object) permissions);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        var user = permissionService.getCurrentUser();
        if (user == null || !permissionService.hasAnyPermission(user, permissions)) {
            log.warn("User {} attempted to access method requiring any of permissions: {}", 
                    authentication.getName(), (Object) permissions);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
                    "Access denied. Required at least one of: " + String.join(", ", permissions));
        }

        return joinPoint.proceed();
    }

    /**
     * Intercept @RequireAdmin annotations (method or class level)
     */
    @Around("@annotation(requireAdmin) || @within(requireAdmin)")
    public Object checkAdmin(ProceedingJoinPoint joinPoint, RequireAdmin requireAdmin) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthenticated access attempt to admin-only method");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        if (!permissionService.hasPermission(authentication, "ALL")) {
            log.warn("User {} attempted to access admin-only method", authentication.getName());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. Admin access required");
        }

        return joinPoint.proceed();
    }
}
