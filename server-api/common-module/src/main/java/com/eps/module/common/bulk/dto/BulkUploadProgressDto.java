package com.eps.module.common.bulk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BulkUploadProgressDto {
    
    private String status; // PROCESSING, COMPLETED, FAILED
    private Integer totalRecords;
    private Integer processedRecords;
    private Integer successCount;
    private Integer failureCount;
    private Double progressPercentage;
    private String message;
    private List<BulkUploadErrorDto> errors;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
