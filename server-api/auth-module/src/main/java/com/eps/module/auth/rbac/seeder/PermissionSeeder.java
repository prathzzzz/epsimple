package com.eps.module.auth.rbac.seeder;

import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.rbac.dto.PermissionDefinition;
import com.eps.module.auth.rbac.registry.PermissionRegistry;
import com.eps.module.auth.repository.PermissionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Seeds all permissions into the database on application startup.
 * Uses PermissionRegistry to get all permission definitions.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1) // Run first
public class PermissionSeeder {

    private final PermissionRepository permissionRepository;
    private final PermissionRegistry permissionRegistry;

    @PostConstruct
    @Transactional
    public void seedPermissions() {
        log.info("Starting permission seeding...");

        List<PermissionDefinition> permissionDefinitions = permissionRegistry.getAllPermissions();
        int created = 0;
        int updated = 0;
        int skipped = 0;

        for (PermissionDefinition def : permissionDefinitions) {
            Optional<Permission> existingPermission = permissionRepository.findByName(def.getName());

            if (existingPermission.isPresent()) {
                // Update description/category if changed
                Permission permission = existingPermission.get();
                boolean changed = false;

                if (!def.getDescription().equals(permission.getDescription())) {
                    permission.setDescription(def.getDescription());
                    changed = true;
                }

                if (!def.getCategory().equals(permission.getCategory())) {
                    permission.setCategory(def.getCategory());
                    changed = true;
                }

                if (changed) {
                    permissionRepository.save(permission);
                    updated++;
                    log.debug("Updated permission: {}", def.getName());
                } else {
                    skipped++;
                }
            } else {
                // Create new permission
                Permission permission = Permission.builder()
                        .name(def.getName())
                        .description(def.getDescription())
                        .scope(def.getScope())
                        .action(def.getAction())
                        .category(def.getCategory())
                        .isSystemPermission(true)
                        .isActive(true)
                        .build();

                permissionRepository.save(permission);
                created++;
                log.debug("Created permission: {}", def.getName());
            }
        }

        log.info("Permission seeding completed. Created: {}, Updated: {}, Skipped: {}, Total: {}",
                created, updated, skipped, permissionDefinitions.size());
    }
}
