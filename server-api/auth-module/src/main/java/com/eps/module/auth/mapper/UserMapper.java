package com.eps.module.auth.mapper;

import com.eps.module.auth.dto.PermissionDTO;
import com.eps.module.auth.dto.RoleDTO;
import com.eps.module.auth.dto.UserDTO;
import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.entity.Role;
import com.eps.module.auth.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "allPermissions", source = "roles", qualifiedByName = "extractAllPermissions")
    UserDTO toDTO(User user);

    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO userDTO);

    RoleDTO toRoleDTO(Role role);

    PermissionDTO toPermissionDTO(Permission permission);

    /**
     * Extract all unique permission names from user's roles
     */
    @Named("extractAllPermissions")
    default List<String> extractAllPermissions(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
