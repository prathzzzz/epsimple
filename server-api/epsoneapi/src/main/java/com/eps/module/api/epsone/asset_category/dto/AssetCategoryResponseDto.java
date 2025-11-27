package com.eps.module.api.epsone.asset_category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetCategoryResponseDto {

    private Long id;
    private String categoryName;
    private String categoryCode;
    private String assetCodeAlt;
    private String description;
    private Double depreciation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
