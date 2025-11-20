package com.eps.module.auth.rbac.service;

import com.eps.module.auth.rbac.dto.PermissionDTO;
import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionManagementService {

    private final PermissionRepository permissionRepository;

    /**
     * Get all active permissions
     */
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findByIsActive(true).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get permissions grouped by category for UI
     */
    public Map<String, List<PermissionDTO>> getPermissionsByCategory() {
        return permissionRepository.findByIsActive(true).stream()
                .map(this::toDTO)
                .collect(Collectors.groupingBy(PermissionDTO::getCategory));
    }

    /**
     * Get permissions by scope
     */
    public List<PermissionDTO> getPermissionsByScope(String scope) {
        return permissionRepository.findByScope(scope).stream()
                .filter(p -> p.getIsActive())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PermissionDTO toDTO(Permission permission) {
        return PermissionDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .scope(permission.getScope())
                .action(permission.getAction())
                .category(permission.getCategory())
                .build();
    }
}
