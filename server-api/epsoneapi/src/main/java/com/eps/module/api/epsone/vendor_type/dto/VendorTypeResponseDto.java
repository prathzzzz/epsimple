package com.eps.module.api.epsone.vendor_type.dto;

import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryResponseDto;
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
    private VendorCategoryResponseDto vendorCategory;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
