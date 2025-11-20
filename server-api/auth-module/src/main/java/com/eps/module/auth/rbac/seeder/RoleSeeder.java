package com.eps.module.auth.rbac.seeder;

import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.entity.Role;
import com.eps.module.auth.repository.PermissionRepository;
import com.eps.module.auth.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Seeds the ADMIN role with all permissions on application startup.
 * Runs after PermissionSeeder to ensure all permissions exist.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(2) // Run after PermissionSeeder
public class RoleSeeder {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    private static final String ADMIN_ROLE_NAME = "ADMIN";

    @PostConstruct
    @Transactional
    public void seedAdminRole() {
        log.info("Starting ADMIN role seeding...");

        // Find the ALL permission
        Optional<Permission> allPermission = permissionRepository.findByName("ALL");
        
        if (allPermission.isEmpty()) {
            log.error("ALL permission not found. Ensure PermissionSeeder runs before RoleSeeder.");
            return;
        }

        Optional<Role> existingAdmin = roleRepository.findByName(ADMIN_ROLE_NAME);

        if (existingAdmin.isPresent()) {
            // Update ADMIN role to ensure it has ALL permission
            Role adminRole = existingAdmin.get();
            Set<Permission> adminPermissions = new HashSet<>();
            adminPermissions.add(allPermission.get());

            if (!adminRole.getPermissions().equals(adminPermissions)) {
                adminRole.setPermissions(adminPermissions);
                roleRepository.save(adminRole);
                log.info("Updated ADMIN role with ALL permission");
            } else {
                log.info("ADMIN role already has ALL permission");
            }
        } else {
            // Create ADMIN role with ALL permission
            Set<Permission> adminPermissions = new HashSet<>();
            adminPermissions.add(allPermission.get());

            Role adminRole = Role.builder()
                    .name(ADMIN_ROLE_NAME)
                    .description("Administrator with complete system access")
                    .isSystemRole(true)
                    .isActive(true)
                    .permissions(adminPermissions)
                    .build();

            roleRepository.save(adminRole);
            log.info("Created ADMIN role with ALL permission");
        }

        log.info("ADMIN role seeding completed.");
    }
}
