package com.eps.module.api.epsone.vendortype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorTypeRequestDto {
    
    @NotBlank(message = "Vendor type name is required")
    @Size(max = 100, message = "Vendor type name must not exceed 100 characters")
    private String typeName;
    
    @NotNull(message = "Vendor category is required")
    private Long vendorCategoryId;
    
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
}
