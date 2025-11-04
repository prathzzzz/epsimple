package com.eps.module.api.epsone.asset_type.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetTypeRequestDto {
    
    @NotBlank(message = "Type name is required")
    @Size(max = 100, message = "Type name must not exceed 100 characters")
    private String typeName;
    
    @NotBlank(message = "Type code is required")
    @Size(max = 20, message = "Type code must not exceed 20 characters")
    private String typeCode;
    
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
}
