package com.eps.module.common.bulk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkUploadErrorDto {
    
    private Integer rowNumber;
    private String fieldName;
    private String errorMessage;
    private String rejectedValue;
}
