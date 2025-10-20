package com.eps.module.api.epsone.vendortype.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorTypeResponseDto {
    private Long id;
    private String typeName;
    private String vendorCategory;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
