package com.eps.module.auth.master.seeder;

import com.eps.module.auth.entity.Permission;
import com.eps.module.auth.repository.PermissionRepository;
import com.eps.module.common.seeder.base.AbstractSeeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(-3)
public class PermissionSeeder extends AbstractSeeder {

    private final PermissionRepository permissionRepository;

    @Override
    public String getSeederName() {
        return "Permissions";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        // Never skip - always check individual permissions
        return false;
    }

    @Override
    protected void performSeeding() {
        log.info("Creating permissions...");
        
        int permissionsCreated = 0;

        // Create READ permission if it doesn't exist
        if (!permissionRepository.findByName("READ").isPresent()) {
            Permission readPermission = Permission.builder()
                    .name("READ")
                    .description("Read permission")
                    .build();
            permissionRepository.save(readPermission);
            permissionsCreated++;
            log.info("Created READ permission");
        } else {
            log.info("READ permission already exists");
        }

        // Create WRITE permission if it doesn't exist
        if (!permissionRepository.findByName("WRITE").isPresent()) {
            Permission writePermission = Permission.builder()
                    .name("WRITE")
                    .description("Write permission")
                    .build();
            permissionRepository.save(writePermission);
            permissionsCreated++;
            log.info("Created WRITE permission");
        } else {
            log.info("WRITE permission already exists");
        }

        // Create UPDATE permission if it doesn't exist
        if (!permissionRepository.findByName("UPDATE").isPresent()) {
            Permission updatePermission = Permission.builder()
                    .name("UPDATE")
                    .description("Update permission")
                    .build();
            permissionRepository.save(updatePermission);
            permissionsCreated++;
            log.info("Created UPDATE permission");
        } else {
            log.info("UPDATE permission already exists");
        }

        // Create DELETE permission if it doesn't exist
        if (!permissionRepository.findByName("DELETE").isPresent()) {
            Permission deletePermission = Permission.builder()
                    .name("DELETE")
                    .description("Delete permission")
                    .build();
            permissionRepository.save(deletePermission);
            permissionsCreated++;
            log.info("Created DELETE permission");
        } else {
            log.info("DELETE permission already exists");
        }

        log.info("Created {} new permissions during this seeding process", permissionsCreated);
    }
}
