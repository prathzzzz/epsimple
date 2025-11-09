package com.eps.module.common.seeder;

import com.eps.module.common.seeder.base.BaseSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeederOrchestrator implements CommandLineRunner {

    private final List<BaseSeeder> seeders;

    @Value("${app.seeder.enabled:false}")
    private boolean seederEnabled;

    @Override
    public void run(String... args) {
        if (!seederEnabled) {
            log.info("Seeding is disabled. Set app.seeder.enabled=true to enable it.");
            return;
        }

        log.info("=".repeat(80));
        log.info("Starting Data Seeding Process");
        log.info("=".repeat(80));

        try {
            // Sort seeders by @Order annotation
            seeders.sort(AnnotationAwareOrderComparator.INSTANCE);

            log.info("Found {} seeders to execute", seeders.size());

            // Execute seeders sequentially and wait for completion
            for (BaseSeeder seeder : seeders) {
                CompletableFuture<Void> future = seeder.seed();
                future.join(); // Wait for each seeder to complete before starting the next
            }

            log.info("=".repeat(80));
            log.info("Data Seeding Process Completed Successfully!");
            log.info("=".repeat(80));

        } catch (Exception e) {
            log.error("=".repeat(80));
            log.error("Data Seeding Process Failed!");
            log.error("Error: {}", e.getMessage(), e);
            log.error("=".repeat(80));
        }
    }
}
