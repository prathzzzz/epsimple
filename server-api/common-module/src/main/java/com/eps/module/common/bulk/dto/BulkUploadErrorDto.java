package com.eps.module.common.bulk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BulkUploadErrorDto {
    
    private Integer rowNumber;
    private String fieldName;
    private String errorMessage;
    private String rejectedValue;
    private String errorType; // VALIDATION, DUPLICATE, ERROR
    private Map<String, Object> rowData; // Complete row data for error reporting
}
