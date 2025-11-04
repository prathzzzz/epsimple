package com.eps.module.api.epsone.cost_type.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostTypeRequestDto {
    
    @NotBlank(message = "Type name is required")
    @Size(max = 100, message = "Type name must not exceed 100 characters")
    private String typeName;
    
    @Size(max = 5000, message = "Type description must not exceed 5000 characters")
    private String typeDescription;
    
    @NotNull(message = "Cost category is required")
    private Long costCategoryId;
}
