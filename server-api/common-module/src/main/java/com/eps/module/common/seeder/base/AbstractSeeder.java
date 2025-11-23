package com.eps.module.common.seeder.base;

import com.eps.module.common.constant.CommonErrorMessages;
import com.eps.module.common.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class AbstractSeeder implements BaseSeeder {

    @Override
    @Async("seederTaskExecutor")
    public CompletableFuture<Void> seed() {
        try {
            log.info("Starting {} seeding...", getSeederName());
            
            if (!isEnabled()) {
                log.info("{} seeding is disabled, skipping...", getSeederName());
                return CompletableFuture.completedFuture(null);
            }

            if (shouldSkipSeeding()) {
                log.info("{} data already exists, skipping seeding...", getSeederName());
                return CompletableFuture.completedFuture(null);
            }

            performSeeding();
            log.info("{} seeding completed successfully!", getSeederName());
            
        } catch (Exception e) {
            log.error("Error occurred while seeding {}: {}", getSeederName(), e.getMessage(), e);
            throw new InternalServerErrorException(CommonErrorMessages.SEEDER_FAILURE + getSeederName(), e);
        }
        
        return CompletableFuture.completedFuture(null);
    }

    protected abstract boolean shouldSkipSeeding();
    protected abstract void performSeeding();

    @Override
    public boolean isEnabled() {
        return true; // Default to enabled, will be controlled by orchestrator
    }
}
