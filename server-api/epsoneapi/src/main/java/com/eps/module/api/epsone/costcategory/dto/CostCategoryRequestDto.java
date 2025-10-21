package com.eps.module.api.epsone.costcategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostCategoryRequestDto {
    
    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name must not exceed 50 characters")
    private String categoryName;
    
    @Size(max = 5000, message = "Category description must not exceed 5000 characters")
    private String categoryDescription;
}
