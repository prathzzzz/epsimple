package com.eps.module.auth.rbac.mapper;

import com.eps.module.auth.audit.AuditUserResolver;
import com.eps.module.auth.rbac.dto.PermissionDTO;
import com.eps.module.auth.rbac.dto.RoleDTO;
import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final AuditUserResolver auditUserResolver;

    public RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }

        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .isSystemRole(role.getIsSystemRole())
                .isActive(role.getIsActive())
                .permissions(role.getPermissions().stream()
                        .map(this::permissionToDTO)
                        .collect(Collectors.toSet()))
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .createdBy(auditUserResolver.resolveUserName(role.getCreatedBy()))
                .updatedBy(auditUserResolver.resolveUserName(role.getUpdatedBy()))
                .build();
    }

    private PermissionDTO permissionToDTO(Permission permission) {
        if (permission == null) {
            return null;
        }

        return PermissionDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .scope(permission.getScope())
                .action(permission.getAction())
                .category(permission.getCategory())
                .createdAt(permission.getCreatedAt())
                .updatedAt(permission.getUpdatedAt())
                .createdBy(auditUserResolver.resolveUserName(permission.getCreatedBy()))
                .updatedBy(auditUserResolver.resolveUserName(permission.getUpdatedBy()))
                .build();
    }
}
