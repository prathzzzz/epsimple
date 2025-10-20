package com.eps.module.auth.mapper;

import com.eps.module.auth.dto.PermissionDTO;
import com.eps.module.auth.dto.RoleDTO;
import com.eps.module.auth.dto.UserDTO;
import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.entity.Role;
import com.eps.module.auth.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO userDTO);

    RoleDTO toRoleDTO(Role role);

    PermissionDTO toPermissionDTO(Permission permission);
}
