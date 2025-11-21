package com.eps.module.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedException(UnauthorizedException ex) {
        log.warn("Unauthorized access attempt: {}", ex.getMessage());
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(BadCredentialsException ex) {
        log.warn("Bad credentials exception: {}", ex.getMessage());
        return ResponseBuilder.error("Invalid email or password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.warn("Username not found: {}", ex.getMessage());
        return ResponseBuilder.error("User not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        log.warn("Validation failed: {}", errors);
        return ResponseBuilder.validationError(errors);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalStateException(IllegalStateException ex) {
        log.warn("Illegal state: {}", ex.getMessage());
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

        @ExceptionHandler(ForeignKeyConstraintException.class)
    public ResponseEntity<ApiResponse<Object>> handleForeignKeyConstraint(ForeignKeyConstraintException ex) {
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Unable to complete operation due to data dependencies.";
        
        if (ex.getMessage() != null) {
            String errorMsg = ex.getMessage();
            
            if (errorMsg.contains("foreign key constraint")) {
                // Extract table names from the error message
                String parentTable = extractTableName(errorMsg, "on table \"(\\w+)\"");
                String childTable = extractTableName(errorMsg, "from table \"(\\w+)\"");
                
                if (parentTable != null && childTable != null) {
                    message = String.format(
                        "Cannot delete this %s because it is being used by %s records. " +
                        "Please remove or reassign the related %s records first.",
                        formatTableName(parentTable),
                        formatTableName(childTable),
                        formatTableName(childTable)
                    );
                } else {
                    message = "Cannot delete this record because it is being used by other records. " +
                             "Please remove the dependencies first.";
                }
                
                log.warn("Foreign key constraint violation: Parent table='{}', Child table='{}', Error: {}", 
                         parentTable, childTable, errorMsg);
            } else if (errorMsg.contains("unique constraint") || errorMsg.contains("duplicate key")) {
                // Extract constraint/column name if available
                String constraintName = extractConstraintName(errorMsg);
                
                if (constraintName != null) {
                    message = String.format(
                        "This record already exists. A record with this %s already exists in the system.",
                        formatConstraintName(constraintName)
                    );
                } else {
                    message = "This record already exists. Please use different values.";
                }
                
                log.warn("Unique constraint violation: {}", errorMsg);
            } else if (errorMsg.contains("not-null constraint")) {
                message = "Required field is missing. Please provide all mandatory information.";
                log.warn("Not-null constraint violation: {}", errorMsg);
            }
        }
        
        return ResponseBuilder.error(message, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Extract table name from PostgreSQL error message using regex pattern
     */
    private String extractTableName(String errorMsg, String pattern) {
        try {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(errorMsg);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception e) {
            log.debug("Failed to extract table name from error message", e);
        }
        return null;
    }
    
    /**
     * Extract constraint name from error message
     */
    private String extractConstraintName(String errorMsg) {
        try {
            // Try to extract constraint name from various formats
            // Format 1: constraint [constraint_name]
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("constraint \\[([\\w_]+)\\]");
            java.util.regex.Matcher m = p.matcher(errorMsg);
            if (m.find()) {
                return m.group(1);
            }
            
            // Format 2: constraint "constraint_name"
            p = java.util.regex.Pattern.compile("constraint \"([\\w_]+)\"");
            m = p.matcher(errorMsg);
            if (m.find()) {
                return m.group(1);
            }
            
            // Format 3: Detail: Key (column_name)=(value) already exists
            p = java.util.regex.Pattern.compile("Key \\(([\\w_]+)\\)=");
            m = p.matcher(errorMsg);
            if (m.find()) {
                return m.group(1);
            }
            
            // Format 4: Constraint name in parentheses or after "violates"
            p = java.util.regex.Pattern.compile("violates.*?constraint.*?\"([\\w_]+)\"");
            m = p.matcher(errorMsg);
            if (m.find()) {
                return m.group(1);
            }
            
            // Format 5: Extract any constraint-like name
            p = java.util.regex.Pattern.compile("([uk]k[ie]\\w+)");
            m = p.matcher(errorMsg);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception e) {
            log.debug("Failed to extract constraint name from error message", e);
        }
        return null;
    }
    
    /**
     * Format table name to be more user-friendly
     * Converts: site_type -> Site Type, payment_details -> Payment Details
     */
    private String formatTableName(String tableName) {
        if (tableName == null) return "record";
        
        // Convert snake_case to Title Case
        String[] words = tableName.split("_");
        StringBuilder formatted = new StringBuilder();
        
        for (String word : words) {
            if (formatted.length() > 0) {
                formatted.append(" ");
            }
            formatted.append(word.substring(0, 1).toUpperCase())
                    .append(word.substring(1).toLowerCase());
        }
        
        return formatted.toString();
    }
    
    /**
     * Format constraint name to be more user-friendly
     * Converts: uk_email -> email, uk_pan_number -> PAN number, ukieguxlae1jyjt45hgxmr6i2yv -> category name
     */
    private String formatConstraintName(String constraintName) {
        if (constraintName == null) return "value";
        
        // Handle hash-based constraint names (generated by database)
        // Common pattern: uk + random hash - likely a unique constraint on first non-id column
        if (constraintName.matches("^uk[a-z0-9]{20,}$")) {
            return "category name or code";
        }
        
        // Remove common prefixes
        String cleaned = constraintName
            .replaceFirst("^(uk_|uq_|unique_)", "")
            .replaceFirst("^(fk_|foreign_key_)", "")
            .replace("_", " ");
        
        // Handle specific known column patterns
        if (cleaned.contains("category name")) {
            return "category name";
        }
        if (cleaned.contains("category code")) {
            return "category code";
        }
        if (cleaned.contains("code")) {
            return "code";
        }
        if (cleaned.contains("name")) {
            return "name";
        }
        
        return cleaned.toLowerCase();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return ResponseBuilder.error("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
