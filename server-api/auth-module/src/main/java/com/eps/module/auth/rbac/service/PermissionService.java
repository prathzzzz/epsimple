package com.eps.module.auth.rbac.service;

import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.entity.Role;
import com.eps.module.auth.entity.User;
import com.eps.module.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for checking user permissions.
 * Supports multi-role permission resolution (user has permission if ANY of their roles grants it).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final UserRepository userRepository;

    /**
     * Check if the currently authenticated user has a specific permission
     */
    public boolean hasPermission(String permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return hasPermission(authentication, permission);
    }

    /**
     * Check if the authenticated user has a specific permission
     */
    public boolean hasPermission(Authentication authentication, String permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        
        if (user == null || !user.getIsActive()) {
            return false;
        }

        return hasPermission(user, permission);
    }

    /**
     * Check if a user has a specific permission
     * User has permission if ANY of their roles grants it OR if user has ALL permission
     */
    public boolean hasPermission(User user, String permission) {
        if (user == null || !user.getIsActive()) {
            return false;
        }

        Set<String> userPermissions = getUserPermissions(user);
        
        // Check if user has ALL permission (super admin)
        if (userPermissions.contains("ALL")) {
            return true;
        }

        return userPermissions.contains(permission);
    }

    /**
     * Check if user has at least one of the specified permissions
     */
    public boolean hasAnyPermission(User user, String... permissions) {
        if (user == null || !user.getIsActive()) {
            return false;
        }

        Set<String> userPermissions = getUserPermissions(user);
        
        // Check if user has ALL permission (super admin)
        if (userPermissions.contains("ALL")) {
            return true;
        }

        for (String permission : permissions) {
            if (userPermissions.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if user has all of the specified permissions
     */
    public boolean hasAllPermissions(User user, String... permissions) {
        if (user == null || !user.getIsActive()) {
            return false;
        }

        Set<String> userPermissions = getUserPermissions(user);
        
        // Check if user has ALL permission (super admin)
        if (userPermissions.contains("ALL")) {
            return true;
        }

        for (String permission : permissions) {
            if (!userPermissions.contains(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get all permission names for a user (union of all permissions from all roles)
     */
    public Set<String> getUserPermissions(User user) {
        if (user == null) {
            return Set.of();
        }

        return user.getRoles().stream()
                .filter(Role::getIsActive)
                .flatMap(role -> role.getPermissions().stream())
                .filter(Permission::getIsActive)
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    /**
     * Check if user is an admin (has ALL permission)
     */
    public boolean isAdmin(User user) {
        return hasPermission(user, "ALL");
    }

    /**
     * Check if currently authenticated user is an admin
     */
    public boolean isAdmin() {
        return hasPermission("ALL");
    }

    /**
     * Get currently authenticated user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
