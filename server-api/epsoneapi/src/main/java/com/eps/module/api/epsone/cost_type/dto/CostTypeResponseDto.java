package com.eps.module.api.epsone.cost_type.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostTypeResponseDto {
    
    private Long id;
    private String typeName;
    private String typeDescription;
    private Long costCategoryId;
    private String costCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
