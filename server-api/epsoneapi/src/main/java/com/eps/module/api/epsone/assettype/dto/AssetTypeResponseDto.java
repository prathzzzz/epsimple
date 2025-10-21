package com.eps.module.api.epsone.assettype.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetTypeResponseDto {
    private Long id;
    private String typeName;
    private String typeCode;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
