package com.eps.module.api.epsone.rbac.service;

import com.eps.module.api.epsone.rbac.dto.PermissionDTO;
import com.eps.module.auth.entity.Role;
import com.eps.module.auth.entity.User;
import com.eps.module.auth.rbac.service.PermissionService;
import com.eps.module.auth.repository.RoleRepository;
import com.eps.module.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        if (!user.getRoles().remove(role)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not have this role");
        }

        userRepository.save(user);
        
        log.info("Removed role {} from user {}", role.getName(), user.getEmail());
    }

    /**
     * Get all permissions for a user (combined from all roles)
     */
    public Set<String> getUserPermissions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return permissionService.getUserPermissions(user);
    }
}
