package com.eps.module.auth.rbac.service;

import com.eps.module.auth.rbac.dto.CreateRoleRequest;
import com.eps.module.auth.rbac.dto.RoleDTO;
import com.eps.module.auth.rbac.dto.UpdateRolePermissionsRequest;
import com.eps.module.auth.rbac.dto.UpdateRoleRequest;
import com.eps.module.auth.rbac.mapper.RoleMapper;
import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.entity.Role;
import com.eps.module.auth.repository.PermissionRepository;
import com.eps.module.auth.repository.RoleRepository;
import com.eps.module.common.exception.BadRequestException;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ForbiddenException;
import com.eps.module.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleManagementService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    /**
     * Get all roles
     */
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get role by ID
     */
    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return roleMapper.toDTO(role);
    }

    /**
     * Create new role (ADMIN only)
     */
    @Transactional
    public RoleDTO createRole(CreateRoleRequest request) {
        // Check if role name already exists
        if (roleRepository.existsByName(request.getName())) {
            throw new ConflictException("Role with this name already exists");
        }

        // Fetch permissions
        Set<Permission> permissions = new HashSet<>(
                permissionRepository.findAllById(request.getPermissionIds())
        );

        if (permissions.size() != request.getPermissionIds().size()) {
            throw new BadRequestException("Some permissions not found");
        }

        // Create role
        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isSystemRole(false)
                .isActive(true)
                .permissions(permissions)
                .build();

        role = roleRepository.save(role);
        log.info("Created role: {}", role.getName());
        
        return roleMapper.toDTO(role);
    }

    /**
     * Update role (ADMIN only)
     */
    @Transactional
    public RoleDTO updateRole(Long id, UpdateRoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        // Prevent updating system roles
        if (role.getIsSystemRole()) {
            throw new ForbiddenException("Cannot update system role");
        }

        // Check if new name conflicts with existing role
        if (!role.getName().equals(request.getName()) && roleRepository.existsByName(request.getName())) {
            throw new ConflictException("Role with this name already exists");
        }

        role.setName(request.getName());
        role.setDescription(request.getDescription());

        role = roleRepository.save(role);
        log.info("Updated role: {}", role.getName());
        
        return roleMapper.toDTO(role);
    }

    /**
     * Delete role (ADMIN only)
     */
    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        // Prevent deleting system roles
        if (role.getIsSystemRole()) {
            throw new ForbiddenException("Cannot delete system role");
        }

        roleRepository.delete(role);
        log.info("Deleted role: {}", role.getName());
    }

    /**
     * Update role permissions (ADMIN only)
     */
    @Transactional
    public RoleDTO updateRolePermissions(Long id, UpdateRolePermissionsRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        // Prevent updating system role permissions
        if (role.getIsSystemRole()) {
            throw new ForbiddenException("Cannot update system role permissions");
        }

        // Fetch permissions
        Set<Permission> permissions = new HashSet<>(
                permissionRepository.findAllById(request.getPermissionIds())
        );

        if (permissions.size() != request.getPermissionIds().size()) {
            throw new BadRequestException("Some permissions not found");
        }

        role.setPermissions(permissions);
        role = roleRepository.save(role);
        log.info("Updated permissions for role: {}", role.getName());
        
        return roleMapper.toDTO(role);
    }
}
