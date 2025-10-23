package com.eps.module.auth.mapper;

import com.eps.module.auth.dto.PermissionDTO;
import com.eps.module.auth.dto.RoleDTO;
import com.eps.module.auth.dto.UserDTO;
import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.entity.Role;
import com.eps.module.auth.entity.User;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-23T21:53:11+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.id( user.getId() );
        userDTO.email( user.getEmail() );
        userDTO.name( user.getName() );
        userDTO.isActive( user.getIsActive() );
        userDTO.roles( roleSetToRoleDTOSet( user.getRoles() ) );

        return userDTO.build();
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( userDTO.getId() );
        user.email( userDTO.getEmail() );
        user.name( userDTO.getName() );
        user.isActive( userDTO.getIsActive() );
        user.roles( roleDTOSetToRoleSet( userDTO.getRoles() ) );

        return user.build();
    }

    @Override
    public RoleDTO toRoleDTO(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleDTO.RoleDTOBuilder roleDTO = RoleDTO.builder();

        roleDTO.id( role.getId() );
        roleDTO.name( role.getName() );
        roleDTO.description( role.getDescription() );
        roleDTO.permissions( permissionSetToPermissionDTOSet( role.getPermissions() ) );

        return roleDTO.build();
    }

    @Override
    public PermissionDTO toPermissionDTO(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionDTO.PermissionDTOBuilder permissionDTO = PermissionDTO.builder();

        permissionDTO.id( permission.getId() );
        permissionDTO.name( permission.getName() );
        permissionDTO.description( permission.getDescription() );

        return permissionDTO.build();
    }

    protected Set<RoleDTO> roleSetToRoleDTOSet(Set<Role> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleDTO> set1 = new LinkedHashSet<RoleDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Role role : set ) {
            set1.add( toRoleDTO( role ) );
        }

        return set1;
    }

    protected Permission permissionDTOToPermission(PermissionDTO permissionDTO) {
        if ( permissionDTO == null ) {
            return null;
        }

        Permission.PermissionBuilder permission = Permission.builder();

        permission.id( permissionDTO.getId() );
        permission.name( permissionDTO.getName() );
        permission.description( permissionDTO.getDescription() );

        return permission.build();
    }

    protected Set<Permission> permissionDTOSetToPermissionSet(Set<PermissionDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Permission> set1 = new LinkedHashSet<Permission>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( PermissionDTO permissionDTO : set ) {
            set1.add( permissionDTOToPermission( permissionDTO ) );
        }

        return set1;
    }

    protected Role roleDTOToRole(RoleDTO roleDTO) {
        if ( roleDTO == null ) {
            return null;
        }

        Role.RoleBuilder role = Role.builder();

        role.id( roleDTO.getId() );
        role.name( roleDTO.getName() );
        role.description( roleDTO.getDescription() );
        role.permissions( permissionDTOSetToPermissionSet( roleDTO.getPermissions() ) );

        return role.build();
    }

    protected Set<Role> roleDTOSetToRoleSet(Set<RoleDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Role> set1 = new LinkedHashSet<Role>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( RoleDTO roleDTO : set ) {
            set1.add( roleDTOToRole( roleDTO ) );
        }

        return set1;
    }

    protected Set<PermissionDTO> permissionSetToPermissionDTOSet(Set<Permission> set) {
        if ( set == null ) {
            return null;
        }

        Set<PermissionDTO> set1 = new LinkedHashSet<PermissionDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Permission permission : set ) {
            set1.add( toPermissionDTO( permission ) );
        }

        return set1;
    }
}
