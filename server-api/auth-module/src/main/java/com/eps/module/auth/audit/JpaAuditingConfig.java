package com.eps.module.auth.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Configuration for JPA Auditing.
 * Provides the current auditor (user ID) for createdBy and updatedBy fields.
 * 
 * IMPORTANT: This implementation retrieves the user ID from the authentication details
 * set by JwtAuthenticationFilter. It does NOT query the database to avoid
 * infinite recursion during entity flush operations.
 */
@Slf4j
@Configuration
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated() 
                    || "anonymousUser".equals(authentication.getPrincipal())) {
                log.debug("No authenticated user found for auditing");
                return Optional.empty();
            }

            // Get user ID from authentication details (set by JwtAuthenticationFilter)
            Object details = authentication.getDetails();
            if (details instanceof AuditingAuthenticationDetails) {
                Long userId = ((AuditingAuthenticationDetails) details).getUserId();
                if (userId != null) {
                    log.debug("Auditor set to user ID: {}", userId);
                    return Optional.of(userId);
                }
            }
            
            log.debug("User ID not available in authentication details for user: {}", authentication.getName());
            return Optional.empty();
        };
    }
}
