package com.eps.module.common.util;

import com.eps.module.common.constants.ErrorMessages;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for common validation operations.
 * Provides reusable methods for entity validation, error message formatting, and constraint checks.
 */
public final class ValidationUtils {
    
    private static final int MAX_NAMES_TO_DISPLAY = 5;
    
    private ValidationUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Formats a not found error message
     */
    public static String formatNotFoundError(String entityName, Object id) {
        return String.format(ErrorMessages.ENTITY_NOT_FOUND_SIMPLE, entityName, id);
    }
    
    /**
     * Formats an already exists error message
     */
    public static String formatAlreadyExistsError(String entityName, String value) {
        return String.format(ErrorMessages.ENTITY_ALREADY_EXISTS, entityName, value);
    }
    
    /**
     * Formats a code already exists error message
     */
    public static String formatCodeAlreadyExistsError(String entityName, String code) {
        return String.format(ErrorMessages.ENTITY_CODE_ALREADY_EXISTS, entityName, code);
    }
    
    /**
     * Formats a cannot delete error message for entities with simple associations
     */
    public static String formatCannotDeleteError(String entityName, long count, String associationType) {
        return String.format(ErrorMessages.CANNOT_DELETE_HAS_ASSOCIATIONS, entityName, count, associationType);
    }
    
    /**
     * Formats a cannot delete error message for entities with assets
     */
    public static String formatCannotDeleteAssetError(String entityType, String entityName, long assetCount) {
        String plural = assetCount > 1 ? "s" : "";
        return String.format(ErrorMessages.CANNOT_DELETE_HAS_ASSETS, 
            entityType, entityName, assetCount, plural, entityType);
    }
    
    /**
     * Formats a detailed cannot delete error message with entity names
     */
    public static String formatCannotDeleteWithNamesError(
            String entityType, 
            String entityName, 
            long totalCount,
            List<String> entityNames,
            String dependentType) {
        
        String plural = totalCount > 1 ? "s" : "";
        StringBuilder errorMessage = new StringBuilder();
        
        errorMessage.append(String.format("Cannot delete '%s' %s because it is being used by %d %s%s: ",
            entityName, entityType, totalCount, dependentType, plural));
        
        // Add first 5 names
        List<String> namesToShow = entityNames.stream()
            .limit(MAX_NAMES_TO_DISPLAY)
            .collect(Collectors.toList());
        errorMessage.append(String.join(", ", namesToShow));
        
        // Add "and X more" if there are more
        if (totalCount > MAX_NAMES_TO_DISPLAY) {
            errorMessage.append(" and ").append(totalCount - MAX_NAMES_TO_DISPLAY).append(" more");
        }
        
        errorMessage.append(". Please delete or reassign these ")
                   .append(dependentType)
                   .append(plural)
                   .append(" first.");
        
        return errorMessage.toString();
    }
    
    /**
     * Builds a full name from person details components
     */
    public static String buildFullName(String firstName, String middleName, String lastName) {
        StringBuilder fullName = new StringBuilder();
        
        if (isNotBlank(firstName)) {
            fullName.append(firstName.trim());
        }
        
        if (isNotBlank(middleName)) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(middleName.trim());
        }
        
        if (isNotBlank(lastName)) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(lastName.trim());
        }
        
        return fullName.length() > 0 ? fullName.toString() : "Unknown";
    }
    
    /**
     * Checks if a string is not null and not blank
     */
    private static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Returns pluralized form of a word if count is greater than 1
     */
    public static String pluralize(String word, long count) {
        return count > 1 ? word + "s" : word;
    }
    
    /**
     * Returns pluralized form of a word if count is greater than 1 with custom plural form
     */
    public static String pluralize(String singular, String plural, long count) {
        return count > 1 ? plural : singular;
    }
}
