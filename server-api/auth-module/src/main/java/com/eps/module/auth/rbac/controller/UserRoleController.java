package com.eps.module.auth.rbac.controller;

import com.eps.module.auth.rbac.service.UserRoleService;
import com.eps.module.auth.rbac.annotation.RequireAdmin;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    /**
     * Assign role to user (ADMIN only)
     */
    @PostMapping("/{userId}/roles/{roleId}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<Void>> assignRoleToUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        userRoleService.assignRoleToUser(userId, roleId);
        return ResponseBuilder.success(null, "Role assigned to user successfully");
    }

    /**
     * Remove role from user (ADMIN only)
     */
    @DeleteMapping("/{userId}/roles/{roleId}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<Void>> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        userRoleService.removeRoleFromUser(userId, roleId);
        return ResponseBuilder.success(null, "Role removed from user successfully");
    }

    /**
     * Get all permissions for a user
     */
    @GetMapping("/{userId}/permissions")
    public ResponseEntity<ApiResponse<Set<String>>> getUserPermissions(@PathVariable Long userId) {
        return ResponseBuilder.success(
            userRoleService.getUserPermissions(userId),
            "User permissions retrieved successfully"
        );
    }
}
