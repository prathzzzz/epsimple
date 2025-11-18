package com.eps.module.crypto.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configuration class for cryptographic operations.
 * Reads encryption key from environment variable.
 */
@Slf4j
@Getter
@Configuration
public class CryptoConfiguration {

    @Value("${crypto.encryption.key:#{null}}")
    private String encryptionKey;

    @PostConstruct
    public void init() {
        // Try to get from environment variable if not set in properties
        if (encryptionKey == null || encryptionKey.trim().isEmpty()) {
            encryptionKey = System.getenv("EPS_ENCRYPTION_KEY");
        }

        if (encryptionKey == null || encryptionKey.trim().isEmpty()) {
            log.error("⚠️ WARNING: EPS_ENCRYPTION_KEY environment variable is not set!");
            log.error("⚠️ Please set the environment variable before running the application");
            log.error("⚠️ Example: setx EPS_ENCRYPTION_KEY \"your-secure-key-here\"");
            throw new IllegalStateException("Encryption key not configured. Set EPS_ENCRYPTION_KEY environment variable.");
        }

        if (encryptionKey.length() < 32) {
            log.error("⚠️ WARNING: Encryption key should be at least 32 characters for AES-256");
            throw new IllegalStateException("Encryption key must be at least 32 characters long");
        }

        log.info("✓ Encryption key loaded successfully (length: {})", encryptionKey.length());
    }
}
