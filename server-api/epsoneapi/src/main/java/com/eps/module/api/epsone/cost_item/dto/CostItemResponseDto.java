package com.eps.module.api.epsone.cost_item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostItemResponseDto {

    private Long id;
    private Long costTypeId;
    private String costTypeName;
    private String costCategoryName;
    private String costItemFor;
    private String itemDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
