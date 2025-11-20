package com.eps.module.api.epsone.rbac.controller;

import com.eps.module.api.epsone.rbac.dto.PermissionDTO;
import com.eps.module.api.epsone.rbac.service.PermissionManagementService;
import com.eps.module.auth.rbac.service.PermissionService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionManagementService permissionManagementService;
    private final PermissionService permissionService;

    /**
     * Get all permissions
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionDTO>>> getAllPermissions() {
        return ResponseBuilder.success(
            permissionManagementService.getAllPermissions(),
            "Permissions retrieved successfully"
        );
    }

    /**
     * Get permissions grouped by category
     */
    @GetMapping("/grouped")
    public ResponseEntity<ApiResponse<Map<String, List<PermissionDTO>>>> getPermissionsByCategory() {
        return ResponseBuilder.success(
            permissionManagementService.getPermissionsByCategory(),
            "Permissions grouped by category retrieved successfully"
        );
    }

    /**
     * Get permissions for a specific scope
     */
    @GetMapping("/scope/{scope}")
    public ResponseEntity<ApiResponse<List<PermissionDTO>>> getPermissionsByScope(@PathVariable String scope) {
        return ResponseBuilder.success(
            permissionManagementService.getPermissionsByScope(scope),
            "Permissions for scope retrieved successfully"
        );
    }

    /**
     * Get current user's permissions
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Set<String>>> getMyPermissions() {
        var user = permissionService.getCurrentUser();
        if (user == null) {
            return ResponseBuilder.success(Set.of(), "No permissions found");
        }
        return ResponseBuilder.success(
            permissionService.getUserPermissions(user),
            "User permissions retrieved successfully"
        );
    }
}
