package com.eps.module.auth.rbac.controller;

import com.eps.module.auth.rbac.dto.CreateRoleRequest;
import com.eps.module.auth.rbac.dto.RoleDTO;
import com.eps.module.auth.rbac.dto.UpdateRolePermissionsRequest;
import com.eps.module.auth.rbac.dto.UpdateRoleRequest;
import com.eps.module.auth.rbac.service.RoleManagementService;
import com.eps.module.auth.rbac.annotation.RequireAdmin;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleManagementService roleManagementService;

    /**
     * Get all roles
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleDTO>>> getAllRoles() {
        return ResponseBuilder.success(
            roleManagementService.getAllRoles(),
            "Roles retrieved successfully"
        );
    }

    /**
     * Get role by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDTO>> getRoleById(@PathVariable Long id) {
        return ResponseBuilder.success(
            roleManagementService.getRoleById(id),
            "Role retrieved successfully"
        );
    }

    /**
     * Create new role (ADMIN only)
     */
    @PostMapping
    @RequireAdmin
    public ResponseEntity<ApiResponse<RoleDTO>> createRole(@Valid @RequestBody CreateRoleRequest request) {
        return ResponseBuilder.success(
            roleManagementService.createRole(request),
            "Role created successfully",
            HttpStatus.CREATED
        );
    }

    /**
     * Update role (ADMIN only)
     */
    @PutMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<RoleDTO>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequest request) {
        return ResponseBuilder.success(
            roleManagementService.updateRole(id, request),
            "Role updated successfully"
        );
    }

    /**
     * Delete role (ADMIN only)
     */
    @DeleteMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        roleManagementService.deleteRole(id);
        return ResponseBuilder.success(null, "Role deleted successfully");
    }

    /**
     * Update role permissions (ADMIN only)
     */
    @PutMapping("/{id}/permissions")
    @RequireAdmin
    public ResponseEntity<ApiResponse<RoleDTO>> updateRolePermissions(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRolePermissionsRequest request) {
        return ResponseBuilder.success(
            roleManagementService.updateRolePermissions(id, request),
            "Role permissions updated successfully"
        );
    }
}
