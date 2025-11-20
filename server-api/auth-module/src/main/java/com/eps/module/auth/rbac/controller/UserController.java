package com.eps.module.auth.rbac.controller;

import com.eps.module.auth.rbac.dto.CreateUserRequest;
import com.eps.module.auth.rbac.dto.UpdateUserRequest;
import com.eps.module.auth.rbac.service.UserManagementService;
import com.eps.module.auth.dto.UserDTO;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;

    /**
     * Get all users (ADMIN only)
     */
    @GetMapping
    @RequireAdmin
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseBuilder.success(
            userManagementService.getAllUsers(),
            "Users retrieved successfully"
        );
    }

    /**
     * Get user by ID (ADMIN only)
     */
    @GetMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        return ResponseBuilder.success(
            userManagementService.getUserById(id),
            "User retrieved successfully"
        );
    }

    /**
     * Create new user (ADMIN only)
     */
    @PostMapping
    @RequireAdmin
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseBuilder.success(
            userManagementService.createUser(request),
            "User created successfully",
            HttpStatus.CREATED
        );
    }

    /**
     * Update user (ADMIN only)
     */
    @PutMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseBuilder.success(
            userManagementService.updateUser(id, request),
            "User updated successfully"
        );
    }

    /**
     * Delete user (ADMIN only)
     */
    @DeleteMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userManagementService.deleteUser(id);
        return ResponseBuilder.success(null, "User deleted successfully");
    }
}
