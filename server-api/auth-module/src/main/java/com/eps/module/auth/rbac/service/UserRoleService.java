package com.eps.module.auth.rbac.service;

import com.eps.module.auth.entity.Role;
import com.eps.module.auth.entity.User;
import com.eps.module.auth.repository.RoleRepository;
import com.eps.module.auth.repository.UserRepository;
import com.eps.module.common.exception.BadRequestException;
import com.eps.module.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    /**
     * Assign role to user (ADMIN only)
     */
    @Transactional
    public void assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.getRoles().add(role);
        userRepository.save(user);
        
        log.info("Assigned role {} to user {}", role.getName(), user.getEmail());
    }

    /**
     * Remove role from user (ADMIN only)
     */
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (!user.getRoles().remove(role)) {
            throw new BadRequestException("User does not have this role");
        }

        userRepository.save(user);
        
        log.info("Removed role {} from user {}", role.getName(), user.getEmail());
    }

    /**
     * Get all permissions for a user (combined from all roles)
     */
    public Set<String> getUserPermissions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return permissionService.getUserPermissions(user);
    }
}
