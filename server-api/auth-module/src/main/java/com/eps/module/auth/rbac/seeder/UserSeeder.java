package com.eps.module.auth.rbac.seeder;

import com.eps.module.auth.constant.AuthErrorMessages;
import com.eps.module.auth.entity.Role;
import com.eps.module.auth.entity.User;
import com.eps.module.auth.repository.RoleRepository;
import com.eps.module.auth.repository.UserRepository;
import com.eps.module.common.seeder.base.AbstractSeeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(3) // Run after PermissionSeeder and RoleSeeder
public class UserSeeder extends AbstractSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seeder.default-password}")
    private String defaultPassword;

    @Value("${app.seeder.admin-email}")
    private String adminEmail;

    @Override
    public String getSeederName() {
        return "Users";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        // Never skip - always check individual users
        return false;
    }

    @Override
    protected void performSeeding() {
        log.info("Creating default users...");

        // Get roles
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException(AuthErrorMessages.ADMIN_ROLE_NOT_FOUND));

        int usersCreated = 0;

        // Create admin user if it doesn't exist
        if (!userRepository.existsByEmail(adminEmail)) {
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);

            User adminUser = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(defaultPassword))
                    .name("Pratham Karia")
                    .isActive(true)
                    .roles(adminRoles)
                    .build();

            userRepository.save(adminUser);
            usersCreated++;
            log.info("Created admin user with email: {}", adminEmail);
        } else {
            log.info("Admin user already exists with email: {}", adminEmail);
        }

        log.info("Created {} new users during this seeding process", usersCreated);
        log.info("Default credentials - Password: {}", defaultPassword);
        log.info("Admin: {}", adminEmail);
    }
}
