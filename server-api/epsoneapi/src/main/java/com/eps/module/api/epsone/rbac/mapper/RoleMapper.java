package com.eps.module.api.epsone.rbac.mapper;

import com.eps.module.api.epsone.rbac.dto.PermissionDTO;
import com.eps.module.api.epsone.rbac.dto.RoleDTO;
import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.entity.Role;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RoleMapper {

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
                .build();
    }
}
