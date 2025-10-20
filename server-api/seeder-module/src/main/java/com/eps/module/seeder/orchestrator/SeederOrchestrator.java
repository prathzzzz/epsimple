package com.eps.module.seeder.orchestrator;

import com.eps.module.common.seeder.base.BaseSeeder;
import com.eps.module.seeder.config.SeederProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeederOrchestrator implements CommandLineRunner {

    private final List<BaseSeeder> seeders;
    private final SeederProperties seederProperties;

    @Override
    public void run(String... args) {
        log.info("===========================================");
        log.info("Seeder Orchestrator Initialized");
        log.info("Total seeders found: {}", seeders.size());
        log.info("===========================================");

        // Sort seeders by @Order annotation
        seeders.sort(AnnotationAwareOrderComparator.INSTANCE);

        // Debug: Log all seeder packages
        log.info("Detected seeders:");
        seeders.forEach(seeder -> {
            String className = seeder.getClass().getName();
            String packageName = seeder.getClass().getPackage().getName();
            log.info("  - {} (class: {}, package: {})", 
                    seeder.getSeederName(), className, packageName);
        });

        // Separate auth seeders from data seeders
        // Auth seeders are: Permissions, Roles, Users (critical for application)
        List<BaseSeeder> authSeeders = seeders.stream()
                .filter(seeder -> {
                    String name = seeder.getSeederName();
                    return name.equals("Permissions") || name.equals("Roles") || name.equals("Users");
                })
                .collect(Collectors.toList());

        List<BaseSeeder> dataSeeders = seeders.stream()
                .filter(seeder -> {
                    String name = seeder.getSeederName();
                    return !name.equals("Permissions") && !name.equals("Roles") && !name.equals("Users");
                })
                .collect(Collectors.toList());

        try {
            // Always run auth seeders (critical for application)
            // Auth seeders must run SYNCHRONOUSLY because they depend on each other
            if (!authSeeders.isEmpty()) {
                log.info("===========================================");
                log.info("Running Auth Seeders (Always Enabled)...");
                log.info("Auth seeders count: {}", authSeeders.size());
                log.info("Running SYNCHRONOUSLY due to dependencies");
                log.info("===========================================");
                runSeedersSynchronously(authSeeders);
                log.info("✓ Auth Seeding Completed!");
            }

            // Run data seeders only if enabled
            if (!dataSeeders.isEmpty()) {
                if (!seederProperties.isEnabled()) {
                    log.info("===========================================");
                    log.info("╔════════════════════════════════════════╗");
                    log.info("║  DATA SEEDING IS DISABLED              ║");
                    log.info("║                                        ║");
                    log.info("║  {} data seeders skipped                ║", String.format("%-2d", dataSeeders.size()));
                    log.info("║                                        ║");
                    log.info("║  To enable, add to properties:         ║");
                    log.info("║  app.seeder.enabled=true               ║");
                    log.info("╚════════════════════════════════════════╝");
                    log.info("===========================================");
                } else {
                    log.info("===========================================");
                    log.info("Running Data Seeders...");
                    log.info("Data seeders count: {}", dataSeeders.size());
                    log.info("===========================================");
                    runSeedersAsynchronously(dataSeeders);
                    log.info("✓ Data Seeding Completed!");
                }
            }

            log.info("===========================================");
            log.info("✓ All Seeding Operations Completed!");
            log.info("===========================================");
        } catch (Exception e) {
            log.error("===========================================");
            log.error("✗ Error during seeding process: {}", e.getMessage(), e);
            log.error("===========================================");
        }
    }

    private void runSeedersSynchronously(List<BaseSeeder> seedersToRun) {
        for (BaseSeeder seeder : seedersToRun) {
            try {
                seeder.seed().join(); // Wait for each seeder to complete
            } catch (Exception e) {
                log.error("Failed to seed {}: {}", seeder.getSeederName(), e.getMessage(), e);
                throw new RuntimeException("Critical seeder failed: " + seeder.getSeederName(), e);
            }
        }
    }

    private void runSeedersAsynchronously(List<BaseSeeder> seedersToRun) {
        List<CompletableFuture<Void>> futures = seedersToRun.stream()
                .map(BaseSeeder::seed)
                .toList();

        // Wait for all seeders to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .exceptionally(ex -> {
                    log.error("One or more seeders failed: {}", ex.getMessage());
                    return null;
                })
                .join();
    }
}
