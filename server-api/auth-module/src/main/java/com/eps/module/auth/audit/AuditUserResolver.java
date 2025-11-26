package com.eps.module.auth.audit;

import com.eps.module.auth.entity.User;
import com.eps.module.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper component for resolving user IDs to user names.
 * Used by MapStruct mappers to convert createdBy/updatedBy IDs to readable names.
 * Includes simple caching to avoid repeated database lookups.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditUserResolver {

    private final UserRepository userRepository;
    
    // Simple cache for user names to avoid repeated DB lookups
    // In production, consider using a proper cache like Caffeine or Redis
    private final Map<Long, String> userNameCache = new ConcurrentHashMap<>();

    /**
     * Resolve a user ID to the user's name.
     * Uses a separate read-only transaction to avoid interfering with the main transaction.
     * 
     * @param userId the user ID to resolve
     * @return the user's name, or null if userId is null or user not found
     */
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public String resolveUserName(Long userId) {
        if (userId == null) {
            return null;
        }

        // Check cache first
        String cachedName = userNameCache.get(userId);
        if (cachedName != null) {
            return cachedName;
        }

        // Lookup from database
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                String name = user.get().getName();
                if (name != null && !name.isEmpty()) {
                    userNameCache.put(userId, name);
                    return name;
                }
                // Fall back to email if name is empty
                String email = user.get().getEmail();
                userNameCache.put(userId, email);
                return email;
            }
        } catch (Exception e) {
            log.warn("Error resolving user name for ID {}: {}", userId, e.getMessage());
            return null;
        }

        log.warn("User not found for ID: {}", userId);
        return null;
    }

    /**
     * Clear the user name cache.
     * Call this when users are updated or deleted.
     */
    public void clearCache() {
        userNameCache.clear();
        log.debug("User name cache cleared");
    }

    /**
     * Remove a specific user from the cache.
     * 
     * @param userId the user ID to remove from cache
     */
    public void evictFromCache(Long userId) {
        if (userId != null) {
            userNameCache.remove(userId);
            log.debug("User {} evicted from name cache", userId);
        }
    }
}
