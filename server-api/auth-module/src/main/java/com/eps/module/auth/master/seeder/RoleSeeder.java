package com.eps.module.auth.master.seeder;

import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.entity.Role;
import com.eps.module.auth.repository.PermissionRepository;
import com.eps.module.auth.repository.RoleRepository;
import com.eps.module.common.seeder.base.AbstractSeeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(-2)
public class RoleSeeder extends AbstractSeeder {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public String getSeederName() {
        return "Roles";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        // Never skip - always check individual roles
        return false;
    }

    @Override
    protected void performSeeding() {
        log.info("Creating roles...");

        // Get permissions
        Permission readPermission = permissionRepository.findByName("READ")
                .orElseThrow(() -> new RuntimeException("READ permission not found"));
        Permission writePermission = permissionRepository.findByName("WRITE")
                .orElseThrow(() -> new RuntimeException("WRITE permission not found"));
        Permission updatePermission = permissionRepository.findByName("UPDATE")
                .orElseThrow(() -> new RuntimeException("UPDATE permission not found"));
        Permission deletePermission = permissionRepository.findByName("DELETE")
                .orElseThrow(() -> new RuntimeException("DELETE permission not found"));

        int rolesCreated = 0;

        // Create ADMIN role if it doesn't exist
        if (!roleRepository.findByName("ADMIN").isPresent()) {
            Set<Permission> adminPermissions = new HashSet<>();
            adminPermissions.add(readPermission);
            adminPermissions.add(writePermission);
            adminPermissions.add(updatePermission);
            adminPermissions.add(deletePermission);

            Role adminRole = Role.builder()
                    .name("ADMIN")
                    .description("Administrator role with full CRUD access")
                    .permissions(adminPermissions)
                    .build();

            roleRepository.save(adminRole);
            rolesCreated++;
            log.info("Created ADMIN role with {} permissions", adminPermissions.size());
        } else {
            log.info("ADMIN role already exists");
        }

        // Create USER role if it doesn't exist
        if (!roleRepository.findByName("USER").isPresent()) {
            Set<Permission> userPermissions = new HashSet<>();
            userPermissions.add(readPermission);
            userPermissions.add(writePermission);

            Role userRole = Role.builder()
                    .name("USER")
                    .description("Standard user role with basic access")
                    .permissions(userPermissions)
                    .build();

            roleRepository.save(userRole);
            rolesCreated++;
            log.info("Created USER role with {} permissions", userPermissions.size());
        } else {
            log.info("USER role already exists");
        }

        log.info("Created {} new roles during this seeding process", rolesCreated);
    }
}
