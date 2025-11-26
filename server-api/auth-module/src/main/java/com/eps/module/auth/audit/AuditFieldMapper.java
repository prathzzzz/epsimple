package com.eps.module.auth.audit;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * MapStruct mapping helper for common audit field conversions.
 * Provides methods to resolve user IDs to user names for createdBy/updatedBy fields.
 */
@Component
@RequiredArgsConstructor
public class AuditFieldMapper {

    private final AuditUserResolver auditUserResolver;

    /**
     * Map a user ID to the user's name.
     * Used by MapStruct mappers for createdBy field.
     * 
     * @param userId the user ID
     * @return the user's name, or null if not found
     */
    @Named("mapCreatedBy")
    public String mapCreatedBy(Long userId) {
        return auditUserResolver.resolveUserName(userId);
    }

    /**
     * Map a user ID to the user's name.
     * Used by MapStruct mappers for updatedBy field.
     * 
     * @param userId the user ID
     * @return the user's name, or null if not found
     */
    @Named("mapUpdatedBy")
    public String mapUpdatedBy(Long userId) {
        return auditUserResolver.resolveUserName(userId);
    }
}
