package com.eps.module.common.bulk.validator;

import com.eps.module.common.bulk.dto.BulkUploadErrorDto;

import java.util.List;

/**
 * Generic interface for validating bulk upload rows
 * @param <T> The DTO type for the row data
 */
public interface BulkRowValidator<T> {
    
    /**
     * Validates a single row of data
     * @param rowData The data to validate
     * @param rowNumber The row number (for error reporting)
     * @return List of validation errors (empty if valid)
     */
    List<BulkUploadErrorDto> validate(T rowData, int rowNumber);
    
    /**
     * Checks if the row already exists in the database
     * @param rowData The data to check
     * @return true if duplicate, false otherwise
     */
    boolean isDuplicate(T rowData);
}
